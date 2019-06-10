package com.zzx.filter;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.zzx.config.JwtConfig;
import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import com.zzx.service.UserService;
import com.zzx.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        boolean giveFlag = false;
        String authHeader = request.getHeader(jwtConfig.getHeader());

        if (authHeader != null && authHeader.startsWith(jwtConfig.getPrefix())) {
            final String authToken = authHeader.substring(jwtConfig.getPrefix().length());//除去前缀，获取token
            String username = jwtTokenUtil.getUsernameFromToken(authToken);//获取token中的用户名
            if (username != null) {  //非法token或token过期
                if (SecurityContextHolder.getContext().getAuthentication() == null) {  //此请求是否校验过

                    UserDetails userDetails = userService.loadUserByUsername(username);  //被封禁的用户将会失去USER权限，只有NORMAL权限

                    if (username.equals(userDetails.getUsername())) {

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                    } else //token校验失败
                        giveFlag = true;
                }
            } else //token校验失败
                giveFlag = true;
        } else  //未携带token
            giveFlag = true;


        if (giveFlag) {
            //token因某原因校验失败,给定游客身份->[游客]角色未写入数据库角色表
            // 省去每个方法上的permitAll注解
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("NORMAL"));
            User user = new User("NORMAL", "NORMAL", authorities);//假定身份
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);//赋予权限
        }

        chain.doFilter(request, response);
    }
}

