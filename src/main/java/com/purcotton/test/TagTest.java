package com.purcotton.test;

import com.purcotton.pojo.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagTest {
    public static void main(String[] args) {
        List<Tag> list1 = new ArrayList<>();
        Tag tag = new Tag();
        tag.setTagId("1");
        tag.setTagCode("aaaa");
        tag.setTagName("haha");
        tag.setMemberBrand("winner");
        tag.setTagType("固定标签");
        list1.add(tag);
        List<Tag> list2 = new ArrayList<>();
        list1.addAll(list2);
    }
}
