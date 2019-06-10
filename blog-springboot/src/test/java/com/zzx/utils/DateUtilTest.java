package com.zzx.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * dateutil 工具类测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DateUtilTest {
    @Autowired
    private DateUtil dateUtil;

    @Test
    public void timeStampToFormat() throws Exception {
        System.out.println(dateUtil.dateDiffMinute(new Date(1558438546591l)));
    }

}