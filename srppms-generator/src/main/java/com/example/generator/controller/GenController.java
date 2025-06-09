package com.example.generator.controller;

import com.example.common.response.R;
import com.example.generator.service.GenService;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.vo.PageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "代码生成器", tags = {"代码生成器"})
@RequestMapping(value = "/gen", method = {RequestMethod.POST, RequestMethod.GET})
public class GenController {
    @Autowired
    private GenService genService;

    @ResponseBody
    @ApiOperation("获取数据库列表-分页")
    @RequestMapping("/getList")
    public R<PageVo> getList(@RequestBody PageBo pageBo) {
        return genService.getList(pageBo);
    }

    @ResponseBody
    @ApiOperation("生成代码")
    @RequestMapping("/{table}")
    public R<String> create(@PathVariable("table") String table) {
        return genService.create(table);
    }
}
