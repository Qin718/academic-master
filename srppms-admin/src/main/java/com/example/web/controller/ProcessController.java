package com.example.web.controller;


import com.example.common.basic.MultiRequestBody;
import com.example.common.response.R;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.bo.ProcessBo;
import com.example.system.domain.vo.PageVo;
import com.example.system.others.perm.PermInter;
import com.example.system.service.ItemService;
import com.example.system.service.ProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(value = "项目审核管理", tags = {"项目审核管理"})
@RequestMapping(value = "/process", method = {RequestMethod.POST, RequestMethod.GET})
public class ProcessController {
    @Autowired
    private ProcessService processService;
    @Autowired
    public ItemService itemService;

    @ResponseBody
    @ApiOperation("获取某一项目的审核人员名单")
    @RequestMapping(value = "/getList/shareUser/{projectId}")
    @PermInter(perm = ":process:getShareUser:shareUser", name = "获取某一项目的审核人员名单", jsjb = "1")
    public R<List<Integer>> getListShareUser(@PathVariable("projectId") Integer projectId) {
        return R.ok(processService.getListShareUser(projectId));
    }

    @ResponseBody
    @ApiOperation("获取搜索审核人员名单")
    @RequestMapping(value = "/getList/allShareUser")
    @PermInter(perm = ":process:getShareUser:allShareUser", name = "获取搜索审核人员名单", jsjb = "1")
    public R<List<Map<String, Object>>> getListAllShareUser() {
        return R.ok(processService.getListAllShareUser());
    }

    @ResponseBody
    @RequestMapping("/updateProcess")
    @ApiOperation("审核人员提交项目审核结果")
    @PermInter(perm = ":process:updateProcess", name = "审核人员提交项目审核结果", jsjb = "5")
    public R<PageVo> updateProcess(@MultiRequestBody ProcessBo processBo, @MultiRequestBody PageBo pageBo) {

        return R.ok(processService.updateProcess(processBo), itemService.getPageVoProcess(pageBo));
    }

    @ResponseBody
    @ApiOperation("更改某一项目的审核人员名单")
    @RequestMapping("/changeUserProcess")
    @PermInter(perm = ":process:changeUserProcess", name = "更改某一项目的审核人员名单", jsjb = "2")
    public R<PageVo> changeUp(@MultiRequestBody List<Integer> userIdS, @MultiRequestBody Integer projectId, @MultiRequestBody PageBo pageBo) {
        return R.ok(processService.changeUp(userIdS, projectId), itemService.getPageVo(pageBo));
    }
}
