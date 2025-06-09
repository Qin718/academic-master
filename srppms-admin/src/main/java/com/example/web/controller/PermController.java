package com.example.web.controller;

import com.example.common.basic.MultiRequestBody;
import com.example.common.response.R;
import com.example.system.domain.entity.Perm;
import com.example.system.others.perm.PermInter;
import com.example.system.service.PermService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "权限管理", tags = {"权限管理"})
@RequestMapping(value = "/perm", method = {RequestMethod.POST, RequestMethod.GET})
public class PermController {
    @Autowired
    private PermService permService;

    @ResponseBody
    @RequestMapping("/getList")
    @ApiOperation("获取权限列表-不分页")
    public R<List<Perm>> getList() {
        return R.ok(permService.getList());
    }

    @ResponseBody
    @ApiOperation("根据角色获取权限列表-不分页")
    @RequestMapping("/getListByRole/{roleId}")
    public R<List<String>> getListByRole(@PathVariable("roleId") Integer roleId) {
        return R.ok(permService.getListByRole(roleId));
    }

    @ResponseBody
    @ApiOperation("修改角色权限表")
    @RequestMapping("/changeRolePerm")
    @PermInter(perm = ":perm:changeRolePerm", name = "修改角色权限表", jsjb = "1")
    public R<String> changeRolePerm(@MultiRequestBody List<String> newPerm,
                                    @MultiRequestBody List<String> oldPerm,
                                    @MultiRequestBody Integer roleId) {
        return permService.changeRolePerm(newPerm, oldPerm, roleId);
    }
}
