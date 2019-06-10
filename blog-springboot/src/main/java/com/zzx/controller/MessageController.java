package com.zzx.controller;


import com.zzx.model.entity.PageResult;
import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import com.zzx.model.pojo.Message;
import com.zzx.service.MessageService;
import com.zzx.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "留言api", description = "留言api", basePath = "/message")
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private FormatUtil formatUtil;


    @ApiOperation(value = "留言", notes = "留言内容")
    @PostMapping
    public Result message(String messageBody) {
        if (!formatUtil.checkStringNull(messageBody))
            return Result.create(StatusCode.ERROR, "参数错误");

        try {
            messageService.saveMessage(messageBody);
            return Result.create(StatusCode.OK, "留言成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "留言失败" + (e.getMessage() == null ? "" : e.getMessage()));
        }
    }


    //管理员删除

    @ApiOperation(value = "管理员删除留言", notes = "留言id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{messageId}")
    public Result deleteMessage(@PathVariable Integer messageId) {
        if (!formatUtil.checkPositive(messageId))
            return Result.create(StatusCode.ERROR, "参数错误");
        messageService.deleteMessageById(messageId);
        return Result.create(StatusCode.OK, "删除成功");
    }

    //分页查询
    @ApiOperation(value = "分页查询留言", notes = "页码+显示数量")
    @GetMapping("/{page}/{showCount}")
    public Result getMessage(@PathVariable Integer page, @PathVariable Integer showCount) {
        if (!formatUtil.checkPositive(page, showCount))
            return Result.create(StatusCode.ERROR, "参数错误");

        PageResult<Message> pageResult =
                new PageResult<>(messageService.getMessageCount(), messageService.findMessage(page, showCount));

        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }

}
