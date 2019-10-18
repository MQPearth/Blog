package com.zzx.controller;

import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ErrorController {


    public static final String FREQUENT_OPERATION = "/frequentOperation";

    @Autowired
    private HttpServletResponse response;

    /**
     * 有匹配的路径，但是路径下的资源不存在
     * 如/img/xxx.jpg
     *
     * @return
     */
    @RequestMapping("/notfound")
    public Result notfound() {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return Result.create(StatusCode.NOTFOUND, "文件不存在");
    }


    @RequestMapping(FREQUENT_OPERATION)
    public Result frequentOperation() {

        return Result.create(StatusCode.REPERROR, "操作过于频繁");
    }


}
