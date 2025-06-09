package com.example.web.controller;

import com.example.common.response.R;
import com.example.system.domain.vo.UpdateVo;
import com.example.system.service.UpdateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "更新日志表管理", tags = {"更新日志表管理"})
@RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
public class UpdateController {

    @Autowired
    private UpdateService updateService;

    @ResponseBody
    @RequestMapping("/getList")
    @ApiOperation("获取更新日志表列表-不分页")
    public R<List<UpdateVo>> getUpdateList() {
        return R.ok(updateService.getUpdateList());
    }


}
