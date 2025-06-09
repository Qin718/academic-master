package com.example.web.controller;

import com.example.common.basic.MultiRequestBody;
import com.example.common.response.R;
import com.example.system.domain.bo.ConfigBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.entity.Config;
import com.example.system.domain.vo.PageVo;
import com.example.system.others.perm.PermInter;
import com.example.system.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(value = "参数管理", tags = {"参数管理"})
@RequestMapping(value = "/config", method = {RequestMethod.POST, RequestMethod.GET})
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @ResponseBody
    @ApiOperation("获取参数列表-分页")
    @RequestMapping("/getPageVo")
    public R<PageVo> getPageVo(@RequestBody PageBo pageBo) {
        return R.ok(configService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("搜索参数列表-分页")
    @RequestMapping("/getPageVo/search")
    public R<PageVo> getPageVoBySearch(@MultiRequestBody ConfigBo configBo, @MultiRequestBody PageBo pageBo) {
        return configService.getPageVoBySearch(configBo, pageBo);
    }

    @ResponseBody
    @ApiOperation("添加参数")
    @RequestMapping("/insert")
    @PermInter(perm = ":config:insert", name = "添加参数", jsjb = "1")
    public R<PageVo> insertConfig(@MultiRequestBody Config config, @MultiRequestBody PageBo pageBo) {
        return R.ok(configService.insertConfig(config), configService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("删除参数")
    @RequestMapping("/delete")
    @PermInter(perm = ":config:delete", name = "删除参数", jsjb = "1")
    public R<PageVo> deleteConfig(@MultiRequestBody List<Integer> list, @MultiRequestBody PageBo pageBo) {
        return R.ok(configService.deleteConfig(list), configService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("修改参数")
    @RequestMapping("/update")
    @PermInter(perm = ":config:update", name = "修改参数", jsjb = "1")
    public R<PageVo> updateConfig(@MultiRequestBody Config config, @MultiRequestBody PageBo pageBo) {
        return R.ok(configService.updateConfig(config), configService.getPageVo(pageBo));
    }

    @ResponseBody
    @RequestMapping("/getMap")
    @ApiOperation("获取参数列表-分组")
    public R<Map<String, List<Config>>> getMap() {
        return R.ok(configService.getMap());
    }
}
