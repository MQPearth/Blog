package com.zzx.log;

import com.zzx.utils.LoggerUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 控制层 日志 切面
 */
@Aspect
@Component
public class ControllerLog {


    @Autowired
    private HttpServletRequest request;

    private Logger logger = LoggerUtil.loggerFactory(this.getClass());

    /**
     * 拦截控制层的所有public方法
     */
    @Pointcut("execution(public * com.zzx.controller.*.*(..))")
    public void log() {
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

        //方法消耗时间
        long start = System.currentTimeMillis();
        Object obj = pjp.proceed();
        long end = System.currentTimeMillis();


        StringBuilder builder = new StringBuilder();
        builder.append("{URL:[").append(request.getRequestURI()).append("],")
                .append("RequestMethod:[").append(request.getMethod()).append("],")
                .append("Args:").append(Arrays.toString(pjp.getArgs())).append(",")
                .append("ReturnValue:[").append(obj == null ? "null" : obj.toString()).append("],")
                .append("Time:[").append(end - start).append("ms],")
                .append("MethodName:[").append(pjp.getSignature()).append("]}");

        logger.info(builder.toString());
        return obj;

    }


}

