package com.purcotton.pojo;

import com.aliyun.odps.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Tag implements Writable {
    //tagId:bigint,tagType:string,memberBrand:string,tagName:string//,disableStatus:bigint
    private String tagId;
    private String tagType;
    private String memberBrand;
    private String tagCode;
    private String tagName;
    private String tagUser;
    private String tagTime;
    private String tagSource;

    public String getTagSource() {
        return tagSource;
    }

    public void setTagSource(String tagSource) {
        this.tagSource = tagSource;
    }

    public String getTagUser() {
        return tagUser;
    }

    public void setTagUser(String tagUser) {
        this.tagUser = tagUser;
    }

    public String getTagTime() {
        return tagTime;
    }

    public void setTagTime(String tagTime) {
        this.tagTime = tagTime;
    }
    // private long disableStatus;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getMemberBrand() {
        return memberBrand;
    }

    public void setMemberBrand(String memberBrand) {
        this.memberBrand = memberBrand;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeBytes(tagId);
        dataOutput.writeBytes(tagType);
        dataOutput.writeBytes(memberBrand);
        dataOutput.writeBytes(tagCode);
        dataOutput.writeBytes(tagName);
        dataOutput.writeBytes(tagUser);
        dataOutput.writeBytes(tagTime);
        dataOutput.writeBytes(tagSource);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        tagId = dataInput.readLine();
        tagType = dataInput.readLine();
        memberBrand = dataInput.readLine();
        tagCode = dataInput.readLine();
        tagName = dataInput.readLine();
        tagUser = dataInput.readLine();
        tagTime = dataInput.readLine();
        tagSource = dataInput.readLine();
    }
}
