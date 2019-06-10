package com.zzx.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


/**
 * FormatCheckUtilTest 工具类测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FormatUtilTest {

    @Autowired
    private FormatUtil formatUtil;

    @Test
    public void checkMail() throws Exception {
        Assert.assertEquals(true, formatUtil.checkMail("111@qq.com"));
    }

    @Test
    public void checkStringNull() {

        Assert.assertEquals(false, formatUtil.checkStringNull("", "1"));
    }

    @Test
    public void getFileFormat() throws Exception {
        System.out.println(formatUtil.getFileFormat("1.png.bmp"));
        System.out.println(formatUtil.getFileFormat("1png"));

    }

    @Test
    public void checkIP() throws Exception {
        Assert.assertEquals(true, formatUtil.checkIP("134.175.116.44"));
        Assert.assertEquals(false, formatUtil.checkIP("256.256.116.44"));
        Assert.assertEquals(false, formatUtil.checkIP("256.256.116.44"));
        Assert.assertEquals(false, formatUtil.checkIP("256.175.256.44"));
        Assert.assertEquals(false, formatUtil.checkIP("256.175.116.256"));
    }

}