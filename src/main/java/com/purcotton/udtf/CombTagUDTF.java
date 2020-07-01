package com.purcotton.udtf;

import com.aliyun.odps.udf.ExecutionContext;
import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.UDTF;
import com.aliyun.odps.udf.annotation.Resolve;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.purcotton.pojo.CombTag;
import com.purcotton.pojo.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Resolve({"string,string,string,string,string->string,*"})
public class CombTagUDTF extends UDTF {

    @Override
    public void setup(ExecutionContext ctx) throws UDFException {

    }

    @Override
    public void process(Object[] args) throws UDFException {
        String memberId = args[0].toString();
        String taskId = args[1].toString();
        String tagList = null;
        if (args[2] != null) {
            tagList = args[2].toString();
        }
        String combTag = args[3].toString();
        String combTagId = args[4].toString();
        Map<String, List<String>> map;
        Map<String, String> tagMap = new HashMap<>();
        //组合标签定义解析
        map = parserComTag(combTag);
        boolean andFlag = false;
        boolean orFlag = false;
        if(null != tagList) {
            JsonParser jsonParser = new JsonParser();
            JsonArray tagListArray = jsonParser.parse(tagList).getAsJsonArray();
            Gson gson = new Gson();
            List<String> crmList = new ArrayList<>();
            for (JsonElement tag1 : tagListArray) {
                Tag tag = gson.fromJson(tag1, Tag.class);
                crmList.add(tag.getTagId());
                tagMap.put(tag.getTagId(),
                        tag.getMemberBrand()+"\t"+//0
                        tag.getTagSource()+"\t"+//1
                        tag.getTagType()+"\t"+//2
                        tag.getTagName()+"\t"+//3
                        tag.getTagCode()+"\t"+//4
                        tag.getTagTime()+"\t"+//5
                        tag.getTagUser()+"\t");//6
            }
            //and 标签集合处理
            List<String> andList = map.get("and");
            if (null != andList && andList.size() != 0) {
                //会员是否在and标签集中
                andFlag = dealComTag_and(crmList, andList);
            }
            //or 标签集合处理
            List<String> orList = map.get("or");
            if (null != orList && orList.size() != 0) {
                //会员是否含有or标签集中的标签
                orFlag = dealComTag_or(crmList, orList);
            }
            //and和or标签集间判断
            if(andFlag || orFlag){//该会员满足组合标签，需要判断是已经含有还是增加
                //若已经存在 不需要输出 只处理需要增加的
                String tagStr = tagMap.get(combTagId);
                if(tagStr == null){
                    forward(memberId,taskId,combTagId,"1");
                }
            }else {//该会员不满足组合标签
                String tagStr = tagMap.get(combTagId);
                if(tagStr != null){ //会员含有，此时需要删除
//                    String[] split = tagStr.split("\t");
                    forward(memberId,taskId,combTagId,"0");//1待新增 0需删除
                }
            }
        }
    }

    private static Map<String, List<String>> parserComTag(String combTag){
        JsonParser jsonParser = new JsonParser();
        JsonArray combTagArray = jsonParser.parse(combTag).getAsJsonArray();
        Gson gson = new Gson();
        HashMap<String, List<String>> map = new HashMap<>();
        for(JsonElement comb:combTagArray){
            CombTag combTag1 = gson.fromJson(comb, CombTag.class);
            String operationType = combTag1.getOperationType();
            List<String> operationTagId = combTag1.getOperationTagId();
            map.put(operationType,operationTagId);
        }
        return map;
    }

    /*
    判断会员是否在and标签集
     */
    private static boolean dealComTag_and(List<String> crmList, List<String> andList){
        //crmList和andList数据比对判断   crmList包含andList中所有元素
        andList.removeAll(crmList);
        return andList.size() == 0;
    }

    /*
    判断会员是否在or标签集
     */
    private static boolean dealComTag_or(List<String> crmList, List<String> orList){
        //crmList和orList数据比对判断    crmList含有任一orList元素
        int cnt = orList.size();
        orList.removeAll(crmList);
        return orList.size() != cnt;
    }

    @Override
    public void close() throws UDFException {

    }

}