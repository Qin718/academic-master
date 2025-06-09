package com.example.web.controller;

import com.example.common.basic.MultiRequestBody;
import com.example.common.response.R;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.bo.RoleBo;
import com.example.system.domain.entity.Role;
import com.example.system.domain.vo.PageVo;
import com.example.system.others.perm.PermInter;
import com.example.system.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "角色管理", tags = {"角色管理"})
@RequestMapping(value = "/role", method = {RequestMethod.POST, RequestMethod.GET})
public class RoleController {
    @Autowired
    private RoleService roleService;

    @ResponseBody
    @ApiOperation("获取角色列表-分页")
    @RequestMapping("/getPageVo")
    public R<PageVo> getPageVo(@RequestBody PageBo pageBo) {
        return R.ok(roleService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("获取角色列表-不分页")
    @RequestMapping("/getList/roleKind")
    public R<List<Role>> getListRoleKind() {
        return R.ok(roleService.getListRoleKind());
    }

    @ResponseBody
    @ApiOperation("搜索角色列表-分页")
    @RequestMapping("/getPageVo/search")
    public R<PageVo> getPageVoSearch(@MultiRequestBody RoleBo roleBo,
                                     @MultiRequestBody PageBo pageBo) {
        return roleService.getPageVoSearch(roleBo, pageBo);
    }

    @ResponseBody
    @ApiOperation("添加角色")
    @RequestMapping("/insert")
    @PermInter(perm = ":role:insert", name = "添加角色", jsjb = "1")
    public R<PageVo> insertRole(@MultiRequestBody RoleBo roleBo,
                                @MultiRequestBody PageBo pageBo) {
        return R.ok(roleService.insertRole(roleBo), roleService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("修改角色")
    @RequestMapping("/update")
    @PermInter(perm = ":role:update", name = "修改角色", jsjb = "1")
    public R<PageVo> update(@MultiRequestBody RoleBo roleBo, @MultiRequestBody PageBo pageBo) {
        return R.ok(roleService.updateRole(roleBo), roleService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("删除角色")
    @RequestMapping("/delete")
    @PermInter(perm = ":role:delete", name = "删除角色", jsjb = "1")
    public R<PageVo> delete(@MultiRequestBody List<Integer> list,
                            @MultiRequestBody PageBo pageBo) {
        return R.ok(roleService.deleteRole(list), roleService.getPageVo(pageBo));
    }
}
