package com.example.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.example.common.response.R;
import com.example.system.domain.bo.LoginBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.bo.UserBo;
import com.example.system.domain.vo.LoginVo;
import com.example.system.domain.vo.PageVo;
import com.example.system.others.perm.PermInter;
import com.example.system.service.LoginService;
import com.example.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SaCheckLogin
@RestController
@Api(value = "登录管理", tags = {"登录管理"})
@RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    @ResponseBody
    @ApiOperation("登录")
    @RequestMapping("/")
    public R<Object> doLogin(@RequestBody LoginBo bo) {
        return loginService.doLogin(bo);
    }

    @ResponseBody
    @ApiOperation("注册")
    @RequestMapping("/register")
    public R<Map<String, Object>> doRegister(@RequestBody UserBo bo) {
        return loginService.doRegister(bo);
    }

    @SaIgnore
    @ResponseBody
    @ApiOperation("退出")
    @RequestMapping("/out")
    public R<String> loginOut() {
        StpUtil.logout();
        return R.ok("退出成功");
    }

    @ResponseBody
    @ApiOperation("强制退出")
    @RequestMapping("/forcedOut/{account}")
    public R<PageVo> forcedOut(@PathVariable("account") String account,
                               @RequestBody PageBo pageBo) {
        userService.forcedOut(account);
        PageVo list = userService.getOnlineList(pageBo);
        return R.ok("强制退出成功", list);
    }

    @ResponseBody
    @ApiOperation("获取登录记录列表-分页")
    @RequestMapping("/getPageVo")
    public R<PageVo> getPageVo(@RequestBody PageBo pageBo) {
        return R.ok(loginService.getPageVo(pageBo));
    }

    @ResponseBody
    @RequestMapping("/getList/Index")
    @ApiOperation("主页回显登录记录-不分页")
    public R<List<LoginVo>> getListIndex() {
        return R.ok(loginService.getListIndex());
    }

    @ResponseBody
    @ApiOperation("模拟登录用户")
    @RequestMapping("/virtualLogin/{id}")
    @PermInter(perm = ":login:virtualLogin", name = "模拟登录", jsjb = "1")
    public R<Map<String, Object>> virtualLogin(@PathVariable("id") Integer id) {
        return R.ok(loginService.virtualLogin(id));
    }

}
