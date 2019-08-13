package com.zzx.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 日志工具
 */
public class LoggerUtil {


    /**
     * 日志工具bean
     *
     * @param clazz
     * @return
     */
    public static Logger loggerFactory(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
