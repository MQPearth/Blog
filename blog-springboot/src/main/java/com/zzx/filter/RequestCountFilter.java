package com.zzx.filter;

import com.zzx.service.RedisService;

import javax.annotation.Resource;
import javax.servlet.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
//@Component
public class RequestCountFilter /*implements Filter */{
   /* private static final Logger log = LoggerFactory.getLogger(RequestCountFilter.class);

    @Resource
    private RedisService redisService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //对filter进行一个初始化
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //doFilter()前的代码是对用户请求进行预处理
        log.info("预处,ip地址为:{}", servletRequest.getRemoteAddr());
        //执行Filter链中的下一个Filter
        filterChain.doFilter(servletRequest, servletResponse);
        //doFilter()后的代码是对用户请求进行后处理
        log.info("后处:{}", 2);
    }

    @Override
    public void destroy() {
        //对filter进行一个销毁
        Filter.super.destroy();
    }*/
}
