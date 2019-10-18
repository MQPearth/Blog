package com.zzx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 站点介绍
 */
@ConfigurationProperties(prefix = "site")
@Component
public class SiteIntroductionConfig {

    private String introduction;//介绍

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
