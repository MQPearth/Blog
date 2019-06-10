package com.zzx.controller;


import com.zzx.model.entity.PageResult;
import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import com.zzx.model.pojo.Code;
import com.zzx.service.CodeService;
import com.zzx.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "邀请码api", description = "邀请码api", basePath = "/code")
@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/code")
public class CodeController {
    @Autowired
    private CodeService codeService;

    @Autowired
    private FormatUtil formatUtil;

    //生成激活码 将生成的激活码返回
    @ApiOperation(value = "生成激活码", notes = "生成激活码")
    @PostMapping
    public Result generateCode() {
        return Result.create(StatusCode.OK, "生成成功", codeService.generateCode());
    }

    //分页查询激活码
    @ApiOperation(value = "分页查询激活码", notes = "页码+显示条数")
    @GetMapping("/{page}/{showCount}")
    public Result findCode(@PathVariable Integer page, @PathVariable Integer showCount) {
        if (!formatUtil.checkPositive(page, showCount))
            return Result.create(StatusCode.ERROR, "参数错误");

        PageResult<Code> pageResult =
                new PageResult<>(codeService.getCodeCount(), codeService.findCode(page, showCount));
        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }

}
