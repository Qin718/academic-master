package com.example.web.controller;

import com.example.common.response.R;
import com.example.system.domain.vo.DictVo;
import com.example.system.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "字典管理", tags = {"字典管理"})
@RequestMapping(value = "/dict", method = {RequestMethod.POST, RequestMethod.GET})
public class DictController {

    @Autowired
    private DictService dictService;

    @ResponseBody
    @ApiOperation("获取字典列表")
    @RequestMapping("/getDictVoList/{dict}")
    public R<List<DictVo>> getDictVoList(@PathVariable("dict") String dict) {
        return R.ok(dictService.getDictVoList(dict));
    }

}
