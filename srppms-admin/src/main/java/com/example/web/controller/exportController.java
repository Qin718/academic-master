package com.example.web.controller;


import com.example.common.response.R;
import com.example.system.others.perm.PermInter;
import com.example.system.service.ExportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(value = "导出管理", tags = {"导出管理"})
@RequestMapping(value = "/export", method = {RequestMethod.POST, RequestMethod.GET})
public class exportController {

    @Autowired
    private ExportService exportEmployees;

    @ResponseBody
    @ApiOperation("导出用户信息列表并生成xls")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @PermInter(perm = ":export:user", name = "导出用户信息,并生成xls表格", jsjb = "1")
    public R<Map<String, Object>> exportEmployees(@RequestBody List<Integer> ids) {
        return exportEmployees.exportEmployees(ids);
    }
}
