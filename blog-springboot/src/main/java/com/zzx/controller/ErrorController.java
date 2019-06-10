package com.zzx.controller;

import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {

    /**
     * 有匹配的路径，但是路径下的资源不存在
     * 如/img.jpg
     *
     * @return
     */
    @RequestMapping("/notfound")
    public Result notfound() {
        return Result.create(StatusCode.NOTFOUND, "文件不存在");
    }
}
