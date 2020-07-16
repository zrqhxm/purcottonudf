package com.purcotton.udf;

import com.aliyun.odps.udf.UDF;
import com.aliyun.odps.udf.annotation.Resolve;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.purcotton.pojo.Tag;
import java.util.ArrayList;
import java.util.List;

@Resolve("string,string->string")
public class MergeTagListUDF extends UDF {
    public String evaluate(String maxStr,String mogStr) {
        //max中会员标签
        List<Tag> tags = maxTag(maxStr,mogStr);
        List<Tag> mogTags = mogTag(mogStr);
        tags.addAll(mogTags);
        Gson gson = new Gson();
        return gson.toJson(tags);
    }

    private List<Tag> mogTag(String mogStr) {
        List<Tag> tags = new ArrayList<>();
        if(null != mogStr && ! "".equals(mogStr)){
            JsonParser jsonParser = new JsonParser();
            JsonArray asJsonArray = jsonParser.parse(mogStr).getAsJsonArray();
            Gson gson = new Gson();
            for(JsonElement tag:asJsonArray){
                Tag tag1 = gson.fromJson(tag, Tag.class);
                // 添加tagSource = 'crm'判断   如果是新官网的直接去掉
                if("crm".equals(tag1.getTagSource())){
                    //1 固定标签 2 手动标签 3 企业标签 4 运营标签
                    if ("2".equals(tag1.getTagType()) || "3".equals(tag1.getTagType())){
                        tags.add(tag1);
                    }
                    if("HUBEI_RECEIVED_MASKS_COUPON".equals(tag1.getTagCode())
                            || "KG_NEW_MEMBER".equals(tag1.getTagCode()) //过滤音乐节活动的标签
                            || "KG_OLD_MEMBER".equals(tag1.getTagCode())
                            || "KG_PARTICIPATING_MEMBER".equals(tag1.getTagCode())
                            || "KG_INTERACTIVE_MEMBER".equals(tag1.getTagCode())){
                        tags.add(tag1);
                    }
                }
            }
            return tags;
        }else{
            return tags;
        }
    }

    /**
     * 固定标签中时间保留
     * @param maxStr
     * @param mogTag
     * @return
     */
    private List<Tag> maxTag(String maxStr,String mogTag){
        List<Tag> tags = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        JsonArray maxJsonArray = jsonParser.parse(maxStr).getAsJsonArray();
        //去除标签保留时间的逻辑
        JsonArray mogJsonArray = new JsonArray();
        if(null != mogTag && !"".equals(mogTag)){
            mogJsonArray = jsonParser.parse(mogTag).getAsJsonArray();
        }
        Gson gson = new Gson();
        for(JsonElement tag:maxJsonArray){
            Tag tag1 = gson.fromJson(tag, Tag.class);
            if(mogJsonArray != null){
                for(JsonElement mog:mogJsonArray){
                    Tag mog_tag = gson.fromJson(mog, Tag.class);
                    if("crm".equals(mog_tag.getTagSource())){  //如果是crm标签就保留原有的更新时间
                        if(tag1.getTagId().equals(mog_tag.getTagId())){
                            String mogTime = mog_tag.getTagTime();
                            if(mogTime != null){
                                tag1.setTagTime(mogTime);
                            }
                        }
                    }
                }
            }
            tags.add(tag1);
        }
        return tags;
    }
}