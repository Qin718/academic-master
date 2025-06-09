package com.example.web.controller;

import com.example.common.basic.MultiRequestBody;
import com.example.common.response.R;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.bo.ProjectBo;
import com.example.system.domain.vo.PageVo;
import com.example.system.others.perm.PermInter;
import com.example.system.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api(value = "项目管理", tags = {"项目管理"})
@RequestMapping(value = "/item", method = {RequestMethod.POST, RequestMethod.GET})
public class ItemController {
    @Autowired
    public ItemService itemService;

    @ResponseBody
    @ApiOperation("获取项目列表-分页")
    @RequestMapping(value = "/getPageVo")
    public R<PageVo> getPageVo(@RequestBody PageBo pageBo) {
        return R.ok(itemService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("搜索项目列表-分页")
    @RequestMapping(value = "/getPageVo/search")
    public R<PageVo> getPageVoSearch(@MultiRequestBody ProjectBo projectBo,
                                     @MultiRequestBody PageBo pageBo) {
        return itemService.getPageVoSearch(projectBo, pageBo);
    }

    @ResponseBody
    @ApiOperation("添加项目")
    @RequestMapping(value = "/insert")
    public R<PageVo> insertItemBo(@MultiRequestBody ProjectBo projectBo,
                                  @MultiRequestBody PageBo pageBo) {
        return R.ok(itemService.insertItemBo(projectBo), itemService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("删除项目")
    @RequestMapping(value = "/delete")
    public R<PageVo> deleteItem(@MultiRequestBody List<Integer> list,
                                @MultiRequestBody PageBo pageBo) {
        return R.ok(itemService.deleteItem(list), itemService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("删除我的项目")
    @RequestMapping(value = "/delete/myItem")
    public R<PageVo> deleteMyItem(@MultiRequestBody String projectId, @MultiRequestBody String itemId, @MultiRequestBody PageBo pageBo) {
        return R.ok(itemService.deleteItem(projectId, itemId), itemService.getPageVoMyItem(pageBo));
    }

    @ResponseBody
    @ApiOperation("修改项目")
    @RequestMapping(value = "/update")
    public R<PageVo> updateItem(@MultiRequestBody ProjectBo projectBo, @MultiRequestBody PageBo pageBo) {
        return R.ok(itemService.updateItem(projectBo), itemService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("修改我的项目")
    @RequestMapping(value = "/update/myItem")
    public R<PageVo> updateMyItem(@MultiRequestBody ProjectBo projectBo, @MultiRequestBody PageBo pageBo) {
        return R.ok(itemService.updateItem(projectBo), itemService.getPageVoMyItem(pageBo));
    }

    @ResponseBody
    @ApiOperation("获取我的项目列表-分页")
    @RequestMapping(value = "/getPageVo/MyItem")
    public R<PageVo> getPageVoMyItem(@RequestBody PageBo pageBo) {
        return R.ok(itemService.getPageVoMyItem(pageBo));
    }

    @ResponseBody
    @ApiOperation("搜索我的项目列表-分页")
    @RequestMapping(value = "/getPageVo/MyItem/search")
    public R<PageVo> getPageVoMyItemSearch(@MultiRequestBody ProjectBo projectBo,
                                           @MultiRequestBody PageBo pageBo) {
        return itemService.getPageVoMyItemSearch(projectBo, pageBo);
    }

    @ResponseBody
    @ApiOperation("获取当前用户审核列表-分页")
    @RequestMapping(value = "/getPageVo/Process")
    @PermInter(perm = ":item:getPageVo:Process", name = "获取当前用户审核列表-分页", jsjb = "2")
    public R<PageVo> getPageVoProcess(@RequestBody PageBo pageBo) {
        return R.ok(itemService.getPageVoProcess(pageBo));
    }

    @ResponseBody
    @ApiOperation("搜索当前用户审核列表-分页")
    @RequestMapping(value = "/getPageVo/Process/search")
    @PermInter(perm = ":item:getPageVo:Process:search", name = "搜索当前用户审核列表-分页", jsjb = "2")
    public R<PageVo> getPageVoProcessSearch(@MultiRequestBody PageBo pageBo,
                                            @MultiRequestBody ProjectBo projectBo) {
        return itemService.getPageVoProcessSearch(pageBo, projectBo);
    }

}
