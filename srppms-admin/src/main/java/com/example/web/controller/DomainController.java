package com.example.web.controller;

import com.example.common.response.R;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.vo.PageVo;
import com.example.system.service.DomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/domain", method = {RequestMethod.POST, RequestMethod.GET})
@Api(value = "实体管理", tags = {"实体管理"})
public class DomainController {

    @Autowired
    public DomainService domainService;

    @ResponseBody
    @ApiOperation("获取实体列表-分页")
    @RequestMapping(value = "/getPageVo")
    public R<PageVo> getPageVo(@RequestBody PageBo pageBo) {
        return R.ok(domainService.getPageVo(pageBo));
    }

}
