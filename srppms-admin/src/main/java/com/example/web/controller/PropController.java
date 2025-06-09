package com.example.web.controller;


import com.example.common.response.R;
import com.example.system.service.PropService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(value = "表格表头管理", tags = {"表格表头管理"})
@RequestMapping(value = "/prop", method = {RequestMethod.POST, RequestMethod.GET})
public class PropController {

    @Autowired
    private PropService propService;

    @ResponseBody
    @ApiOperation("获取表格表头")
    @RequestMapping("/{table}")
    public R<List<Map<String, Object>>> getProp(@PathVariable("table") String table) {
        return R.ok(propService.getProp(table));
    }
}
