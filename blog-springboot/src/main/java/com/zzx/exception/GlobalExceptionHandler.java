package com.zzx.exception;

import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;


/**
 * 异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 找不到资源 -> com.zzx.config.ErrorConfig
     * 未找到处理器 异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result noHandlerFoundExceptionHander(Exception e) {
//        e.printStackTrace();
        return Result.create(StatusCode.NOTFOUND, "接口不存在");
    }


    /**
     * 权限不足
     *
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result accessDeniedExceptionHander(Exception e) {
//        e.printStackTrace();
        return Result.create(StatusCode.ACCESSERROR, "拒绝访问");
    }

    /**
     * 请求方式错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result httpRequestMethodNotSupportedExceptionHandler(Exception e) {
//        e.printStackTrace();
        return Result.create(StatusCode.ERROR, "请求方式错误");
    }


    /**
     * controller参数异常/缺少
     *
     * @param e
     * @return
     */
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            RequestRejectedException.class}
    )
    public Result missingServletRequestParameterException(Exception e) {
//        e.printStackTrace();
        return Result.create(StatusCode.ERROR, "参数异常");

    }

    /**
     * 单次上传文件过大
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result maxUploadSizeExceededException(Exception e) {
//        e.printStackTrace();
        return Result.create(StatusCode.ERROR, "文件过大");
    }

    /**
     * 客户端错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ClientAbortException.class)
    public Result clientAbortExceptionException(Exception e) {
//        e.printStackTrace();
        return Result.create(StatusCode.ERROR, "客户端错误");
    }


    /**
     * 其他异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result exceptionHander(Exception e) {
        e.printStackTrace();
        return Result.create(StatusCode.SERVICEERROR, "服务异常 请联系管理员");
    }

}

