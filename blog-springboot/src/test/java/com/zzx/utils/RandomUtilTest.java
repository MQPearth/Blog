package com.zzx.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * RandomUtilTest 工具类测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RandomUtilTest {
    @Autowired
    private RandomUtil randomUtil;

    @Test
    public void nextInt() throws Exception {
        for (int i = 0; i < 100; i++) {
            int a = randomUtil.nextInt(1, 5);
            System.out.println(a >= 5 ? a + "大于" : a);
        }

    }

}