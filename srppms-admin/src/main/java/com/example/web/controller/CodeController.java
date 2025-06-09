package com.example.web.controller;

import com.example.common.response.R;
import com.example.system.service.CodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Api(value = "验证码管理", tags = {"验证码管理"})
@RequestMapping(value = "/code", method = {RequestMethod.POST, RequestMethod.GET})
public class CodeController {

    @Autowired
    private CodeService codeService;

    @ResponseBody
    @RequestMapping("/")
    @ApiOperation("获取验证码")
    public R<Map<String, Object>> getCode() {
        return codeService.getCode();
    }

    @ResponseBody
    @ApiOperation("获取邮箱验证码")
    @RequestMapping("/mail/{mail}")
    public R<String> getCodeMail(@PathVariable("mail") String qq) {
        return codeService.getCodeMail(qq);
    }

}
