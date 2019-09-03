package com.nowcoder.community;

import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.SensitiveFilter;
import org.apache.commons.lang3.CharUtils;
import org.junit.Test;

public class testUtil {

    @Test
    public void testUUID() {
        System.out.println(CommunityUtil.generateUUID());
    }

    @Test
    public void testReadRes() {
        SensitiveFilter sensitiveFilter = new SensitiveFilter();
        sensitiveFilter.init();
    }

    @Test
    public void testSensitiveFilter() {
        SensitiveFilter sensitiveFilter = new SensitiveFilter();
        sensitiveFilter.init();

        String sentence = "我要%开%票%， 我要###赌$$$(((博";
        System.out.println(sensitiveFilter.filter(sentence));
    }

    @Test
    public void simpleTest() {
        String a = "我";
        for (int i = 0; i < a.length(); i++) {
            System.out.println(CharUtils.isAsciiAlphanumeric(a.charAt(i)));
            System.out.println(a.charAt(i));
        }
    }
}
