package com.zzx.log;

import com.zzx.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    @Autowired
    private RabbitTemplate rabbitTemplate;


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
        PASS_PATH.add("/log/findNewestLog");
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

        String value = obj == null ? "null" : obj.toString();
        long time = end - start;
        if (!PASS_PATH.contains(requestUri)) {
            String logInfo = "{URL:[" + requestUri + "]," +
                    "RequestMethod:[" + request.getMethod() + "]," +
                    "Args:" + Arrays.toString(pjp.getArgs()) + "," +
                    "ReturnValue:[" + value + "]," +
                    "Time:[" + time + "ms]," +
                    "MethodName:[" + pjp.getSignature() + "]}";
            log.info(logInfo);

            try {
                com.zzx.model.entity.ControllerLog controllerLog = new com.zzx.model.entity.ControllerLog();
                controllerLog.setUrl(requestUri);
                controllerLog.setRequestMethod(request.getMethod());
                controllerLog.setArgs(Arrays.toString(pjp.getArgs()));
                controllerLog.setReturnValue(value);
                controllerLog.setTime(Long.valueOf(time).intValue());
                controllerLog.setMethodName(pjp.getSignature().toString());
                controllerLog.setDate(new Date());

                rabbitTemplate.convertAndSend(RabbitMqConfig.CONTROLLER_LOG_QUEUE, controllerLog);
            } catch (Throwable t) {
                log.error("日志消息发送失败", t);
            }
        }

        return obj;

    }


}

