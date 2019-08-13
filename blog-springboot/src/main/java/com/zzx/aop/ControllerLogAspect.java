package com.zzx.aop;

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
public class ControllerLogAspect {


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


        String format =
                String.format
                        ("{URL:[%s],RequestMethod:[%s],Args:%s,ReturnValue:[%s],Time:[%sms],MethodName:[%s]}",
                                request.getRequestURI(), request.getMethod(), Arrays.toString(pjp.getArgs()), obj == null ? "null" : obj.toString(), end - start, pjp.getSignature());
        logger.info(format);
        return obj;

    }


}

