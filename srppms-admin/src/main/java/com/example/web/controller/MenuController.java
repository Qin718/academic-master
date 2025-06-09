package com.example.web.controller;

import com.example.common.basic.MultiRequestBody;
import com.example.common.response.R;
import com.example.system.domain.bo.MenuBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.entity.Menu;
import com.example.system.domain.vo.MenuVo;
import com.example.system.domain.vo.PageVo;
import com.example.system.others.perm.PermInter;
import com.example.system.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单操作
 **/
@RestController
@Api(value = "菜单管理", tags = {"菜单管理"})
@RequestMapping(value = "/menu", method = {RequestMethod.POST, RequestMethod.GET})
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ResponseBody
    @ApiOperation("侧边栏动态获取菜单")
    @RequestMapping("/getList/asideMenus")
    public R<List<MenuVo>> getAsideMenus() {
        return R.ok(menuService.getAsideMenus());
    }

    @ResponseBody
    @ApiOperation("根据角色获取菜单列表的ID")
    @RequestMapping("/getListByRole/{roleId}")
    public R<List<Integer>> getListByRole(@PathVariable("roleId") Integer roleId) {
        return R.ok(menuService.getListByRole(roleId));
    }

    @ResponseBody
    @ApiOperation("修改角色菜单表")
    @RequestMapping("/changeRoleMenu")
    @PermInter(perm = ":menu:changeRoleMenu", name = "修改角色菜单表", jsjb = "1")
    public R<String> changeRoleMenu(@MultiRequestBody List<Integer> newMenu,
                                    @MultiRequestBody List<Integer> oldMenu,
                                    @MultiRequestBody Integer roleId) {
        return menuService.changeRoleMenu(newMenu, oldMenu, roleId);
    }

    @ResponseBody
    @ApiOperation("获取父菜单列表-不分页")
    @RequestMapping("/getList/MenuParent")
    public R<List<Menu>> getListMenuParent() {
        return R.ok(menuService.getListMenuParent());
    }

    @ResponseBody
    @ApiOperation("获取菜单列表-分页")
    @RequestMapping("/getPageVo")
    public R<PageVo> getPage(@RequestBody PageBo pageBo) {
        return R.ok(menuService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("模糊搜索菜单列表-分页")
    @RequestMapping("/getPageVo/search")
    public R<PageVo> getPageVoSearchByName(@MultiRequestBody MenuBo menuBo, @MultiRequestBody PageBo pageBo) {
        return menuService.getPageVoSearchByName(menuBo, pageBo);
    }

    @ResponseBody
    @RequestMapping("/getList")
    @ApiOperation("获取菜单列表-不分页")
    public R<List<Menu>> getList() {
        return R.ok(menuService.getMenuRedis());
    }

    @ResponseBody
    @ApiOperation("更新菜单")
    @RequestMapping("/update")
    public R<PageVo> updateMenu(@MultiRequestBody MenuBo menuBo,
                                @MultiRequestBody PageBo pageBo) {
        return R.ok(menuService.updateMenu(menuBo), menuService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("添加菜单")
    @RequestMapping(path = "/insert")
    @PermInter(perm = ":menu:insert", name = "添加菜单", jsjb = "1")
    public R<PageVo> insertMenu(@MultiRequestBody MenuBo menuBo,
                            @MultiRequestBody PageBo pageBo) {
        return R.ok(menuService.insertMenu(menuBo), menuService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("删除菜单")
    @RequestMapping("/delete")
    @PermInter(perm = ":menu:delete", name = "删除菜单", jsjb = "1")
    public R<PageVo> deleteMenu(@MultiRequestBody List<Integer> list,
                                @MultiRequestBody PageBo pageBo) {
        return R.ok(menuService.deleteMenu(list), menuService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("获取按钮列表")
    @RequestMapping("/getButton")
    public R<List<MenuVo>> getButton() {
        return R.ok(menuService.getButton());
    }

}
