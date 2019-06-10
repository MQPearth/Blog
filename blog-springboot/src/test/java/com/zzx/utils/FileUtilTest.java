package com.zzx.utils;

import com.zzx.config.ImgUploadConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * fileutil 测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUtilTest {


    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private ImgUploadConfig imgUploadConfig;

    @Test
    public void initUploadFolder() throws Exception {
        fileUtil.initUploadFolder();
    }

    @Test
    public void getFolder() throws Exception {
        LinkedList<File> files = new LinkedList<>(fileUtil.getAllFolder());
        for (File file : files) {
            System.out.println(file.getPath());
        }
    }

    @Test
    public void getSavePath() throws Exception {
        String path = fileUtil.getSavePath();
        System.out.println(path);
    }
}