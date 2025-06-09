package com.example.web.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.common.basic.MultiRequestBody;
import com.example.common.response.R;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.bo.UserBo;
import com.example.system.domain.vo.PageVo;
import com.example.system.others.perm.PermInter;
import com.example.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(value = "用户管理", tags = {"用户管理"})
@RequestMapping(value = "/user", method = {RequestMethod.POST, RequestMethod.GET})
public class UserController {
    @Autowired
    private UserService userService;

    @ResponseBody
    @ApiOperation("获取用户列表-分页")
    @RequestMapping("/getPageVo")
    public R<PageVo> getPageVo(@RequestBody PageBo pageBo) {
        return R.ok(userService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("获取在线用户列表-分页")
    @RequestMapping("/Online/getList")
    @PermInter(perm = ":user:Online:getList", name = "获取在线用户列表", jsjb = "1")
    public R<PageVo> getOnlineList(@RequestBody PageBo pageBo) {
        return R.ok(userService.getOnlineList(pageBo));
    }

    @ResponseBody
    @ApiOperation("模糊搜索用户列表-分页")
    @RequestMapping("/getPageVo/search")
    public R<PageVo> getPageVoSearch(@MultiRequestBody UserBo userBo,
                                     @MultiRequestBody PageBo pageBo) {
        return userService.getPageVoSearch(userBo, pageBo);
    }

    @ResponseBody
    @ApiOperation("获取用户头像")
    @RequestMapping("/userImage")
    public R<byte[]> getUserImage() {
        String account = (String) StpUtil.getLoginId();
        return R.ok(userService.getUserImage(account));
    }

    @ResponseBody
    @RequestMapping("/getInfo")
    @ApiOperation("主页-回显个人信息")
    public R<Map<String, Object>> getInfo() {
        return R.ok(userService.getInfo());
    }

    @ResponseBody
    @ApiOperation("更改账号状态")
    @RequestMapping("/ChangeStatus")
    @PermInter(perm = ":user:ChangeStatus", name = "更改账号状态", jsjb = "2")
    public R<PageVo> changeStatus(@MultiRequestBody UserBo bo, @MultiRequestBody PageBo pageBo) {
        return R.ok(userService.changeStatus(bo), userService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("批量删除用户")
    @RequestMapping("/delete")
    public R<PageVo> deleteList(@MultiRequestBody List<Integer> list,
                                @MultiRequestBody PageBo pageBo) {
        return R.ok(userService.deleteList(list), userService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("添加用户")
    @RequestMapping(path = "/insert")
    public R<PageVo> insertUser(@MultiRequestBody UserBo userBo,
                                @MultiRequestBody PageBo pageBo) {
        return R.ok(userService.insertUser(userBo), userService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("修改密码")
    @RequestMapping(path = "/updatePwd")
    public R<String> updatePwd(@RequestBody UserBo bo) {
        return userService.updatePwd(bo);
    }

    @ResponseBody
    @ApiOperation("更改账号信息")
    @RequestMapping(path = "/update")
    public R<PageVo> update(@MultiRequestBody UserBo userBo, @MultiRequestBody PageBo pageBo) {
        return R.ok(userService.updateUser(userBo), userService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("更改个人信息")
    @RequestMapping(path = "/updateUserInfo")
    public R<String> updateUserInfo(@RequestBody UserBo bo) {
        return userService.updateUserInfo(bo);
    }

}
