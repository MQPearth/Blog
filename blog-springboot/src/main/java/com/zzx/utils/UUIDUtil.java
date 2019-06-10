package com.zzx.utils;


import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDUtil {

    public String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
