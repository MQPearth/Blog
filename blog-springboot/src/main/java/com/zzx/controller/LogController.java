package com.zzx.controller;

import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import com.zzx.model.vo.NewestLogVO;
import com.zzx.service.LogService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 日志api
 *
 * @author zzx
 * @date 2021-05-29 13:26
 */
@Api(tags = "日志api")
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    LogService logService;

    @PostMapping("/findNewestLog")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result findNewestLog(NewestLogVO vo) {
        if (ObjectUtils.isEmpty(vo.getSize())) {
            vo.setSize(10);
        }
        if (1 == vo.getType()) {
            return Result.create(StatusCode.OK, "ok", logService.findControllerNewestLog
                    (vo.getLeft(), vo.getRight(), vo.getSize()));
        } else {
            return Result.create(StatusCode.OK, "ok", logService.findSqlNewestLog
                    (vo.getLeft(), vo.getRight(), vo.getSize()));
        }
    }
}