package com.zzx.utils;

import com.zzx.config.ImgUploadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 文件操作工具
 */
@Component
public class FileUtil {
    @Autowired
    private ImgUploadConfig imgUploadConfig;

    /**
     * 获取可用的文件保存路径
     * 当所有路径文件夹单位数都超过FolderSize时，返回null
     *
     * @return
     */
    public String getSavePath() {

        ConcurrentLinkedQueue<File> availablePath = ImgUploadConfig.getAvailablePath();
        Iterator<File> iterator = availablePath.iterator();
       
        while (iterator.hasNext()) { 
			File file = iterator.next();
            if (file.listFiles().length < imgUploadConfig.getFolderSize()) {
                return file.getPath();
            } else {
                availablePath.remove(file);
            }
        }
        return null;
    }

    /**
     * 初始化上传文件夹
     * ！操作非常耗时
     *
     * @return
     */
    public List<File> initUploadFolder() {
        File root = new File(imgUploadConfig.getUploadFolder());
        root.mkdirs();
        LinkedList<File> files = new LinkedList<>();
        files.add(root);

        for (int i = 0; i < imgUploadConfig.getLayerCount(); i++) {
            LinkedList<File> filesClone = (LinkedList<File>)files.clone();
            for (File file : filesClone) {
                files.addAll(createFolder(file.getPath(), imgUploadConfig.getFolderSize()));//addAll 添加到链表末尾
            }
        }

        return files;
    }

    /**
     * 创建文件夹
     *
     * @param path
     * @param folderSize 文件夹个数
     */
    private List<File> createFolder(String path, int folderSize) {
        LinkedList<File> files = new LinkedList<>();
        for (int i = 1; i <= folderSize; i++) {
            File file = new File(path + "/" + i);
            file.mkdirs();
            files.add(file);
        }
        //返回创建的文件夹
        return files;
    }


    /**
     * 获取上传文件夹的最下层路径
     *
     * @return
     */
    public List<File> getAllFolder() {
        LinkedList<File> files = new LinkedList<>();
        File root = new File(imgUploadConfig.getUploadFolder());
        files.add(root);
        for (int i = 0; i < imgUploadConfig.getLayerCount(); i++) {
            LinkedList<File> filesClone = (LinkedList<File>)files.clone();
            for (File file : filesClone) {
                files.removeFirst();
                Collections.addAll(files, file.listFiles());
            }
        }
        return files;
    }
}
