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

// TODO define input and output types, e.g. "string,string->string,bigint".
@Resolve({"string,string->string,*"})
public class TagListTransUDTF extends UDTF {

    @Override
    public void setup(ExecutionContext ctx) throws UDFException {

    }

    @Override
    public void process(Object[] args) throws UDFException {
        String memberId = (String)args[0];
        String tagListStr = (String)args[1];
        if(tagListStr != null){
            //过滤mall,解析出tagCode
            JsonParser jsonParser = new JsonParser();
            JsonArray asJsonArray = jsonParser.parse(tagListStr).getAsJsonArray();
            Gson gson = new Gson();
            for(JsonElement tagStr:asJsonArray){
                Tag tag = gson.fromJson(tagStr, Tag.class);
                if("crm".equals(tag.getTagSource())){
                    forward(memberId,tag.getTagId(),tag.getTagTime());
                }
            }
        }else{
            forward(memberId,null,null);
        }

    }

    @Override
    public void close() throws UDFException {

    }

}