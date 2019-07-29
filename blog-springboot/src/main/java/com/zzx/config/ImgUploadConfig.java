package com.zzx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 文件上传相关属性
 **/
@Component
@ConfigurationProperties(prefix = "upload")
public class ImgUploadConfig {

    //静态资源对外暴露的访问路径
    private String staticAccessPath;
    //文件上传目录
    private String uploadFolder;
    //文件夹层数
    private Integer layerCount;
    //文件夹内单位数
    private Integer folderSize;

    private static ConcurrentLinkedQueue<File> availablePath = new ConcurrentLinkedQueue<>();

    public static ConcurrentLinkedQueue<File> getAvailablePath() {
        return availablePath;
    }

    public String getStaticAccessPath() {
        return staticAccessPath;
    }

    public void setStaticAccessPath(String staticAccessPath) {
        this.staticAccessPath = staticAccessPath;
    }

    public String getUploadFolder() {
        return uploadFolder;
    }

    public void setUploadFolder(String uploadFolder) {
        this.uploadFolder = uploadFolder;
    }

    public Integer getLayerCount() {
        return layerCount;
    }

    public void setLayerCount(Integer layerCount) {
        this.layerCount = layerCount;
    }

    public Integer getFolderSize() {
        return folderSize;
    }

    public void setFolderSize(Integer folderSize) {
        this.folderSize = folderSize;
    }
}