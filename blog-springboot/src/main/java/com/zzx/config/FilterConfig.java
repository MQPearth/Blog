package com.zzx.config;

import com.zzx.filter.RequestCountFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 */
//@Configuration
public class FilterConfig {
 
    /*@Bean
    public FilterRegistrationBean testFilter3RegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new RequestCountFilter());
        registration.addUrlPatterns("/blog");
        registration.setOrder(1); // 值越小越靠前，此处配置有效
        return registration;
    }*/
 
}