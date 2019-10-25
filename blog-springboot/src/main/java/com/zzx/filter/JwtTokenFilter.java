package com.zzx.filter;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.zzx.config.JwtConfig;
import com.zzx.config.RedisConfig;
import com.zzx.controller.ErrorController;
import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import com.zzx.service.UserService;
import com.zzx.utils.JwtTokenUtil;
import com.zzx.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private RequestUtil requestUtil;

    /**
     * 范围时间内限制最大请求次数
     */
    private static final int LIMIT_REQUEST_FREQUENCY_COUNT = 8;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        limitRequestFrequency(request, response);


        boolean giveFlag = false;
        String authHeader = request.getHeader(jwtConfig.getHeader());

        if (authHeader != null && authHeader.startsWith(jwtConfig.getPrefix())) {
            UserDetails userDetails = userService.loadUserByToken(authHeader);

            if (null != userDetails) {
                //此请求是否校验过
                if (SecurityContextHolder.getContext().getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            } else {
                giveFlag = true;
            }
        } else {
            //token校验失败
            giveFlag = true;
        }

        if (giveFlag) {
            //token因某原因校验失败,给定游客身份->[游客]角色未写入数据库角色表
            // 省去每个方法上的permitAll注解
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("NORMAL"));
            //假定身份
            User user = new User("NORMAL", "NORMAL", authorities);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            //赋予权限
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    /**
     * 校验请求是否过于频繁
     *
     * @param request
     */
    private void limitRequestFrequency(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String ipAddress = requestUtil.getIpAddress(request);
        String redisKey = RedisConfig.REDIS_IP_PREFIX + ipAddress;
        //缓存时间 2s
        // 127.0.0.1_/blog/hotBlog
        if (redisTemplate.hasKey(redisKey)) {
            String value = redisTemplate.opsForValue().get(redisKey);
            Integer count = Integer.parseInt(value);
            if (count > JwtTokenFilter.LIMIT_REQUEST_FREQUENCY_COUNT) {
                //请求频繁
                request.getRequestDispatcher(ErrorController.FREQUENT_OPERATION).forward(request, response);
            } else {
                count++;
                redisTemplate.opsForValue().set(redisKey, count.toString(), RedisConfig.REDIS_LIMIT_REQUEST_FREQUENCY_TIME, TimeUnit.MILLISECONDS);
            }

        } else {
            redisTemplate.opsForValue().set(redisKey, "1", RedisConfig.REDIS_LIMIT_REQUEST_FREQUENCY_TIME, TimeUnit.MILLISECONDS);
        }
    }
}

