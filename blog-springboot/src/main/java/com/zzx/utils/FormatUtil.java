package com.zzx.utils;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FormatUtil {


    private static final Pattern mailPattern = Pattern.compile("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}");//邮箱格式

    private static final Pattern ipPattern = Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");//ip格式

    /**
     * 邮箱格式校验
     *
     * @param mail
     * @return
     */
    public boolean checkMail(String mail) {
        Matcher m = mailPattern.matcher(mail);
        return m.matches();
    }

    /**
     * 一般只适用于controller的参数校验
     * 检查字符串是否 为 null 为 ""
     * 为null 或 ""都返回 false
     *
     * @param strs 动态参数
     */
    public boolean checkStringNull(String... strs) {
        for (String str : strs) {
            if (str == null || "".equals(str))
                return false;
        }
        return true;
    }

    /**
     * 用于controller的参数校验
     * 检查对象是否 为 null
     *
     * @param objs 动态参数
     */
    public boolean checkObjectNull(Object... objs) {
        for (Object obj : objs) {
            if (obj == null)
                return false;
        }
        return true;
    }


    /**
     * 获取文件格式
     *
     * @param fileName 完整文件名
     * @return
     */
    public String getFileFormat(String fileName) {
        String[] formatNames = fileName.split("\\.");
        if (formatNames == null || formatNames.length <= 1)
            return null;
        String format = "." + formatNames[formatNames.length - 1];
        return format;
    }


    /**
     * 检查数字是否为非负数 >= 0
     *
     * @param numbers
     * @return true -> 全部数字为非负数
     */
    public boolean checkNotNegative(Integer... numbers) {
        for (Integer number : numbers) {
            if (number < 0)
                return false;
        }
        return true;
    }

    /**
     * 检查数字是否为正数 >0
     *
     * @param numbers
     * @return true -> 全部数字为正数
     */
    public boolean checkPositive(Integer... numbers) {
        for (Integer number : numbers) {
            if (number <= 0)
                return false;
        }
        return true;
    }

    public boolean hasObject(Collection<? extends Object> collections, Object obj) {

        for (Object o : collections) {

        }

        return false;
    }

    public boolean checkIP(String name) {
        Matcher m = ipPattern.matcher(name);
        return m.matches();
    }
}
