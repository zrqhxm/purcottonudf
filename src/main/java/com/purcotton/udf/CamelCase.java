package com.purcotton.udf;

import com.aliyun.odps.data.Struct;
import com.aliyun.odps.udf.UDF;
import com.aliyun.odps.udf.annotation.Resolve;
import com.google.gson.Gson;
import com.purcotton.pojo.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * 驼峰命名处理
 */
@Resolve("array<struct<tagId:string,tagType:string,memberBrand:string,tagCode:string,tagName:string,tagUser:string,tagSource:string,tagTime:string>>->string")
public class CamelCase extends UDF {

    public String evaluate(List<Struct> maxTag) {
        List<Tag> tags = new ArrayList<>();
        Tag tag;
        Gson gson = new Gson();
        for (Struct struct: maxTag){
            tag = new Tag();
            String tagId = (String) struct.getFieldValue("tagid");
            String tagType = (String) struct.getFieldValue("tagtype");
            String memberBrand = (String) struct.getFieldValue("memberbrand");
            String tagCode = (String) struct.getFieldValue("tagcode");
            String tagName = (String) struct.getFieldValue("tagname");
            String tagUser = (String) struct.getFieldValue("taguser");
            String tagTime = (String) struct.getFieldValue("tagtime");
            String tagSource = (String) struct.getFieldValue("tagsource");
            tag.setTagId(tagId);
            tag.setTagType(tagType);
            tag.setMemberBrand(memberBrand);
            tag.setTagCode(tagCode);
            tag.setTagName(tagName);
            tag.setTagUser(tagUser);
            tag.setTagTime(tagTime);
            tag.setTagSource(tagSource);
            tags.add(tag);
        }
        String s = gson.toJson(tags);
        return s;
    }
}