package com.example.web.controller;

import com.example.common.basic.MultiRequestBody;
import com.example.common.response.R;
import com.example.system.domain.bo.LogBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.vo.PageVo;
import com.example.system.others.perm.PermInter;
import com.example.system.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "日志管理", tags = {"日志管理"})
@RequestMapping(value = "/log", method = {RequestMethod.POST, RequestMethod.GET})
public class LogController {

    @Autowired
    private LogService logService;

    @ResponseBody
    @ApiOperation("获取日志列表-分页")
    @RequestMapping("/getPageVo")
    public R<PageVo> getPageVo(@RequestBody PageBo pageBo) {
        return R.ok(logService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("搜索日志列表-分页")
    @RequestMapping("/getPageVo/search")
    public R<PageVo> getPageVoSearch(@MultiRequestBody LogBo bo,
                                     @MultiRequestBody PageBo pageBo) {
        return logService.getPageVoSearch(bo, pageBo);
    }

    @ResponseBody
    @ApiOperation("删除日志")
    @RequestMapping("/delete")
    @PermInter(perm = ":log:delete", name = "删除日志", jsjb = "1")
    public R<PageVo> deleteLog(@MultiRequestBody List<Integer> list, @MultiRequestBody PageBo pageBo) {
        return R.ok(logService.deleteLog(list), logService.getPageVo(pageBo));
    }
}
