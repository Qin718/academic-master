package com.example.web.controller;

import com.example.common.basic.MultiRequestBody;
import com.example.common.response.R;
import com.example.system.domain.bo.ActivationBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.vo.ActivationVo;
import com.example.system.domain.vo.PageVo;
import com.example.system.service.ActivationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "活跃度表管理", tags = {"活跃度表管理"})
@RequestMapping(value = "/activation", method = {RequestMethod.POST, RequestMethod.GET})
public class ActivationController {

    @Autowired
    private ActivationService activationService;

    @ResponseBody
    @RequestMapping("/getPageVo")
    @ApiOperation("获取活跃度表列表-分页")
    public R<PageVo> getPageVo(@RequestBody PageBo pageBo) {
        return activationService.getPageVo(pageBo);
    }

    @ResponseBody
    @RequestMapping("/getList")
    @ApiOperation("获取活跃度表列表-不分页")
    public R<List<ActivationVo>> getList() {
        return R.ok(activationService.getList());
    }

    @ResponseBody
    @RequestMapping("/update")
    @ApiOperation("更新活跃度表")
    public R<PageVo> updateActivation(@MultiRequestBody ActivationBo activationBo, @MultiRequestBody PageBo pageBo) {
        return activationService.updateActivation(activationBo,pageBo);
    }

    @ResponseBody
    @RequestMapping("/insert")
    @ApiOperation("添加活跃度表")
    public R<PageVo> insertActivation(@MultiRequestBody ActivationBo activationBo, @MultiRequestBody PageBo pageBo) {
        return activationService.insertActivation(activationBo,pageBo);
    }

    @ResponseBody
    @RequestMapping("/delete/{id}")
    @ApiOperation("删除活跃度表")
    public R<PageVo> deleteActivation(@PathVariable("id") Integer id, @MultiRequestBody PageBo pageBo) {
        return activationService.deleteActivation(id,pageBo);
    }

}
