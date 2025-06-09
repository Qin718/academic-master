package com.example.web.controller;

import com.example.common.response.R;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.vo.PageVo;
import com.example.system.service.InterfaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "接口管理", tags = {"接口管理"})
@RequestMapping(value = "/interface", method = {RequestMethod.POST, RequestMethod.GET})
public class InterfaceController {

    @Autowired
    private InterfaceService interfaceService;

    @ResponseBody
    @RequestMapping("/getPageVo")
    @ApiOperation("获取接口信息列表-分页")
    public R<PageVo> getPageVo(@RequestBody PageBo pageBo) {
        return R.ok(interfaceService.getPageVo(pageBo));
    }

}
