package com.purcotton.udtf;

import com.aliyun.odps.udf.ExecutionContext;
import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.UDTF;
import com.aliyun.odps.udf.annotation.Resolve;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.purcotton.pojo.Tag;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 获取会员标签中变化的标签
 */
@Resolve({"string,string,string->string,*"})
public class TagChangesUDTF extends UDTF {

    @Override
    public void setup(ExecutionContext ctx) throws UDFException {

    }

    @Override
    public void process(Object[] args) throws UDFException {
        //操作类型 1新增 0删除
        //1 固定标签 2 手动标签 3 企业标签 4 运营标签 5 组合标签
        /*
        {
            "tagId": "YYBQ10087",
            "tagSource": "crm",
            "tagType": "4",
            "memberBrand": "purcotton",
            "tagName": "非付费会员",
            "tagCode": "YYBQ10087",
            "tagTime": "2020-04-16 14:06:53",
            "tagUser": "member"
        }
         */
        String memberId = (String)args[0];
        String dw_mergeTagList = (String)args[1];//当天处理好的tagList
        String ads_mergeTagList = (String)args[2];//回流的tagList
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(date);
        if(dw_mergeTagList != null){
            //1.以crm的会员数据为准
            JsonParser jsonParser = new JsonParser();
            JsonArray dw_list = jsonParser.parse(dw_mergeTagList).getAsJsonArray();
            Gson gson = new Gson();
            //ads_mergeTagList 为空，说明是新增的会员标签或标签
            if(ads_mergeTagList == null){
                for(JsonElement tagStr:dw_list){
                    Tag tag = gson.fromJson(tagStr, Tag.class);
                    forward(memberId,
                            tag.getMemberBrand(),
                            tag.getTagId(),
                            tag.getTagSource(),
                            tag.getTagType(),
                            tag.getTagName(),
                            tag.getTagCode(),
                            tag.getTagTime(),
                            tag.getTagUser(),
                            "0",//标签状态，0未更新
                            "1",
                            createTime);//操作 1待新增 0需删除
                }
            }else {
                //3.dw_mergeTagList 和 ads_mergeTagList 不为空，需要循环对比出是新增还是删除
                HashMap<String, String> map_dw = new HashMap<>();
                HashMap<String, String> map_ads = new HashMap<>();
                Set<String> list_dw = new HashSet<>();
                Set<String> list_ads = new HashSet<>();
                Set<String> list_tmp = new HashSet<>();
                JsonParser dw_parser = new JsonParser();
                JsonArray ads_list = dw_parser.parse(ads_mergeTagList).getAsJsonArray();
                for (JsonElement dwTag:dw_list){
                    Tag dw_tag = gson.fromJson(dwTag, Tag.class);
                    list_dw.add(dw_tag.getTagId());
                    map_dw.put(dw_tag.getTagId(),
                            dw_tag.getMemberBrand()+"\t"+
                            dw_tag.getTagSource()+"\t"+
                            dw_tag.getTagType()+"\t"+
                            dw_tag.getTagName()+"\t"+
                            dw_tag.getTagCode()+"\t"+
                            dw_tag.getTagTime()+"\t"+
                            dw_tag.getTagUser());
                    list_tmp.add(dw_tag.getTagId());
                }
                for (JsonElement adsTag:ads_list){
                    Tag ads_tag = gson.fromJson(adsTag,Tag.class);
                    if(!ads_tag.getTagType().equals("5")){//过滤标签集类型
                        list_ads.add(ads_tag.getTagId());
                        map_ads.put(
                                ads_tag.getTagId(),
                                ads_tag.getMemberBrand()+"\t"+//0
                                ads_tag.getTagSource()+"\t"+//1
                                ads_tag.getTagType()+"\t"+//2
                                ads_tag.getTagName()+"\t"+//3
                                ads_tag.getTagCode()+"\t"+//4
                                ads_tag.getTagTime()+"\t"+//5
                                ads_tag.getTagUser()//6
                        );
                    }
                }
                list_dw.removeAll(list_ads);//新增的标签
                list_ads.removeAll(list_tmp);//需要删除的标签
                if(list_dw.size() != 0){
                    for (String dw_tagId:list_dw){
                        String[] split = map_dw.get(dw_tagId).split("\t");
                        forward(memberId,
                                split[0],
                                dw_tagId,
                                split[1],
                                split[2],
                                split[3],
                                split[4],
                                split[5],
                                split[6],
                                "0",
                                "1",
                                createTime);
                    }
                }
                if(list_ads.size() != 0){
                    for (String ads_tagId:list_ads){
                        String[] split = map_ads.get(ads_tagId).split("\t");
                        forward(memberId,
                                split[0],
                                ads_tagId,
                                split[1],
                                split[2],
                                split[3],
                                split[4],
                                split[5],
                                split[6],
                                "0",
                                "0",
                                createTime);
                    }
                }
            }
        }else{
            //2.dw_mergeTagList为空，说明该会员标签全部需要删除
            if(ads_mergeTagList != null){
                JsonParser ads_parser = new JsonParser();
                JsonArray ads_list = ads_parser.parse(ads_mergeTagList).getAsJsonArray();
                Gson gson = new Gson();
                for(JsonElement adsTag:ads_list){
                    Tag ads_tag = gson.fromJson(adsTag, Tag.class);
                    if(!ads_tag.getTagType().equals("5")){//过滤标签集
                        forward(memberId,
                                ads_tag.getMemberBrand(),
                                ads_tag.getTagId(),
                                ads_tag.getTagSource(),
                                ads_tag.getTagType(),
                                ads_tag.getTagName(),
                                ads_tag.getTagCode(),
                                ads_tag.getTagTime(),
                                ads_tag.getTagUser(),
                                "0",
                                "0",
                                createTime);//需删除
                    }

                }
            }
        }
    }

    @Override
    public void close() throws UDFException {

    }

}