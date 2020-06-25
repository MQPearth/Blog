package com.zzx.log;

import com.zzx.utils.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 控制层 日志 切面
 */
@Aspect
@Component
@Slf4j
public class ControllerLog {


    @Autowired
    private HttpServletRequest request;


    /**
     * 拦截控制层的所有public方法
     */
    @Pointcut("execution(public * com.zzx.controller.*.*(..))")
    public void log() {
    }

    /**
     * 不打印日志的路径
     */
    private static final Set<String> PASS_PATH = new HashSet<>(1);

    static {
        PASS_PATH.add("/user/getMailSendState");
    }

    /**
     * 方法执行前后 拦截
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("log()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        String requestUri = request.getRequestURI();

        //方法消耗时间
        long start = System.currentTimeMillis();
        Object obj = pjp.proceed();
        long end = System.currentTimeMillis();
        if (!PASS_PATH.contains(requestUri)) {
            StringBuilder builder = new StringBuilder();
            builder.append("{URL:[").append(requestUri).append("],")
                    .append("RequestMethod:[").append(request.getMethod()).append("],")
                    .append("Args:").append(Arrays.toString(pjp.getArgs())).append(",")
                    .append("ReturnValue:[").append(obj == null ? "null" : obj.toString()).append("],")
                    .append("Time:[").append(end - start).append("ms],")
                    .append("MethodName:[").append(pjp.getSignature()).append("]}");
            log.info(builder.toString());
        }

        return obj;

    }


}

