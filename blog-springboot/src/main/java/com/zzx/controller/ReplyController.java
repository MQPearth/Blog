package com.zzx.controller;

import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import com.zzx.service.ReplyService;
import com.zzx.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "回复api", description = "回复api", basePath = "/reply")
@RestController
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private FormatUtil formatUtil;

    @ApiOperation(value = "发布回复", notes = "回复内容+评论id (父回复节点)?")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{discussId}")
    public Result reply(@PathVariable Integer discussId, String replyBody, Integer rootId) {

        if (!formatUtil.checkStringNull(replyBody))
            return Result.create(StatusCode.ERROR, "参数异常");
        if (!formatUtil.checkPositive(discussId))
            return Result.create(StatusCode.ERROR, "参数异常");

        try {
            replyService.saveReply(discussId, replyBody, rootId);
            return Result.create(StatusCode.OK, "回复成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "回复失败" + e.getMessage());
        }
    }

    @ApiOperation(value = "删除回复", notes = "回复id")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{replyId}")
    public Result deleteReply(@PathVariable Integer replyId) {
        if (!formatUtil.checkPositive(replyId))
            return Result.create(StatusCode.ERROR, "参数错误");

        try {
            replyService.deleteReply(replyId);
            return Result.create(StatusCode.OK, "删除成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "删除失败" + e.getMessage());
        }
    }

    @ApiOperation(value = "管理员删除回复", notes = "回复id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/{replyId}")
    public Result adminDeleteDiscuss(@PathVariable Integer replyId) {
        if (!formatUtil.checkPositive(replyId))
            return Result.create(StatusCode.ERROR, "参数错误");

        try {
            replyService.adminDeleteReply(replyId);
            return Result.create(StatusCode.OK, "删除成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "删除失败" + e.getMessage());
        }

    }
}
