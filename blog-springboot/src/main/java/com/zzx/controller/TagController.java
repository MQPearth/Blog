package com.zzx.controller;

import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import com.zzx.service.TagService;
import com.zzx.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Api(tags = "标签api", description = "标签api", basePath = "/tag")
@RestController
@RequestMapping("/tag")
public class TagController {


    @Autowired
    private TagService tagService;

    @Autowired
    private FormatUtil formatUtil;

    /**
     * 新增一个标签
     *
     * @param tagName 标签名
     * @return
     */
    @ApiOperation(value = "新增标签", notes = "标签名")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public Result newTag(String tagName) {
        if (!formatUtil.checkStringNull(tagName))
            return Result.create(StatusCode.ERROR, "参数异常");
        try {
            tagService.saveTag(tagName);
            return Result.create(StatusCode.OK, "新增成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "新增失败," + e.getMessage());
        }
    }

    /**
     * 删除一个标签
     *
     * @param tagId 标签id
     * @return
     */
    @ApiOperation(value = "删除标签", notes = "标签id")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{tagId}")
    public Result deleteTag(@PathVariable Integer tagId) {
        if (!formatUtil.checkObjectNull(tagId))
            return Result.create(StatusCode.ERROR, "参数异常");
        try {
            tagService.deleteTagById(tagId);
            return Result.create(StatusCode.OK, "删除成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }


    /**
     * 修改一个标签
     *
     * @param tagId   标签id
     * @param tagName 新标签名
     * @return
     */
    @ApiOperation(value = "修改标签", notes = "标签id+新标签名")
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping
    public Result updateTag(Integer tagId, String tagName) {
        if (!formatUtil.checkObjectNull(tagId))
            return Result.create(StatusCode.ERROR, "参数异常");
        if (!formatUtil.checkStringNull(tagName))
            return Result.create(StatusCode.ERROR, "参数异常");

        try {
            tagService.updateTag(tagId, tagName);
            return Result.create(StatusCode.OK, "修改成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }


    /**
     * 获取某用户下的所有标签
     *
     * @return
     */
    @ApiOperation(value = "获取用户标签", notes = "用户id")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public Result findTagByUserId() {
        return Result.create(StatusCode.OK, "查询成功", tagService.findTagByUserId());
    }

}
