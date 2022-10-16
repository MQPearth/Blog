package com.zzx.controller;

import com.zzx.model.entity.PageResult;
import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import com.zzx.model.pojo.Announcement;
import com.zzx.service.AnnouncementService;
import com.zzx.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "公告api", description = "公告api", basePath = "/announcement")
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private FormatUtil formatUtil;


    @ApiOperation(value = "不发布", notes = "公告标题+公告内容")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public Result newAnnouncement(String title, String body) {
        if (!formatUtil.checkStringNull(title, body)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }

        if (title.length() > 255) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }

        announcementService.saveAnnouncement(title, body);
        return Result.create(StatusCode.OK, "发布成功");
    }

    @ApiOperation(value = "删除公告", notes = "公告id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{announcementId}")
    public Result deleteAnnouncement(@PathVariable Integer announcementId) {
        if (!formatUtil.checkPositive(announcementId)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }

        announcementService.deleteAnnouncementById(announcementId);
        return Result.create(StatusCode.OK, "删除成功");
    }

    //置顶
    @ApiOperation(value = "置顶/取消置顶公告", notes = "公告id+置顶状态 0置顶 1正常")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/top/{announcementId}/{top}")
    public Result top(@PathVariable Integer announcementId, @PathVariable Integer top) {
        if (!formatUtil.checkPositive(announcementId)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        if (!formatUtil.checkObjectNull(top)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }

        announcementService.updateAnnouncementTop(announcementId, top);
        return Result.create(StatusCode.OK, "操作成功");
    }


    //分页获取公告

    @ApiOperation(value = "分页查询公告", notes = "页码+显示条数")
    @GetMapping("/{page}/{showCount}")
    public Result getAnnouncement(@PathVariable Integer page, @PathVariable Integer showCount) {
        if (!formatUtil.checkPositive(page, showCount)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }

        PageResult<Announcement> pageResult =
                new PageResult<>(announcementService.getAnnouncementCount(), announcementService.findAnnouncement(page, showCount));

        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }


}
