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

/**
 * 粉丝信息标签过滤
 */
@Resolve("string,string->string")
public class FanMergeTagUDF extends UDF {

    public String evaluate(String maxStr,String mogStr) {
        List<Tag> tags = maxTag(maxStr,mogStr);
        List<Tag> mogTags = mogTag(mogStr);
        tags.addAll(mogTags);
        Gson gson = new Gson();
        String s = gson.toJson(tags);
        return s;
    }

    private List<Tag> mogTag(String mogStr) {
        List<Tag> tags = new ArrayList<>();
        if(null != mogStr && ! "".equals(mogStr)){
            JsonParser jsonParser = new JsonParser();
            JsonArray asJsonArray = jsonParser.parse(mogStr).getAsJsonArray();
            Gson gson = new Gson();
            for(JsonElement tag:asJsonArray){
                Tag tag1 = gson.fromJson(tag, Tag.class);
                //1 固定标签 2 手动标签 3 企业标签 4 运营标签
                if (!"1".equals(tag1.getTagType())){
                    tags.add(tag1);
                }
            }
            return tags;
        }else{
            return tags;
        }
    }

    private List<Tag> maxTag(String maxStr,String mogTag){
        List<Tag> tags = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        JsonArray maxJsonArray = jsonParser.parse(maxStr).getAsJsonArray();
        JsonArray mogJsonArray = new JsonArray();
        if(null != mogTag && ! "".equals(mogTag)){
            mogJsonArray = jsonParser.parse(mogTag).getAsJsonArray();
        }
        Gson gson = new Gson();
        for(JsonElement tag:maxJsonArray){
            Tag tag1 = gson.fromJson(tag, Tag.class);
            if(mogJsonArray != null){
                for(JsonElement mog:mogJsonArray){
                    Tag mog_tag = gson.fromJson(mog, Tag.class);
                    if(tag1.getTagId().equals(mog_tag.getTagId())){
                        String mogTime = mog_tag.getTagTime();
                        if(mogTime != null){
                            tag1.setTagTime(mogTime);
                        }
                    }
                }
            }
            tags.add(tag1);
        }
        return tags;
    }

}