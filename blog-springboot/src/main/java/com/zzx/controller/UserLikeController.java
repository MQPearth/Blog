package com.zzx.controller;

import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import com.zzx.model.pojo.UserLike;
import com.zzx.service.UserLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 用户点赞
 * @author: Tyson
 * @time: 2020-05-26 13:26
 */
@Api(tags = "用户点赞api", description = "用户点赞api", basePath = "/userLike")
@RestController
@RequestMapping("/userLike")
public class UserLikeController {

    @Autowired
    private UserLikeService userLikeService;

    /**
     * @Description: 保存点赞数据
     * @Param: userLike
     * @return: Result
     * @Author: Tyson
     * @Date: 2020/5/26/0026
     */
    @ApiOperation(value = "保存点赞数据")
    @PostMapping("/saveUserLike")
    @PreAuthorize("hasAuthority('USER')")
    public Result saveUserLike(UserLike userLike) {
        if (userLikeService.getUserLike(userLike.getBlog().getId())) {
            return Result.create(StatusCode.ERROR, "你已点过赞");
        }

        try {

            userLikeService.saveUserLike(userLike);
            return Result.create(StatusCode.OK, "点赞记录保存成功");
        } catch (RuntimeException re) {
            return Result.create(StatusCode.ERROR, re.getMessage());
        }
    }

    /**
     * @Description: 判断用户是否点过赞
     * @Param: [blogId]
     * @return: com.zzx.model.entity.Result
     * @Author: Tyson
     * @Date: 2020/5/30/0030 14:01
     */
    @ApiOperation(value = "用户是否点过赞")
    @GetMapping("/isUserLike/{blogId}")
    @PreAuthorize("hasAuthority('USER')")
    public Result getUserLike(@PathVariable Integer blogId) {
        try {
            return Result.create(StatusCode.OK, "获取点赞记录成功", userLikeService.getUserLike(blogId));
        } catch (RuntimeException re) {
            return Result.create(StatusCode.ERROR, re.getMessage());
        }
    }
}