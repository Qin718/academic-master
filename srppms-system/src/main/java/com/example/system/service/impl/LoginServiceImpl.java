package com.example.system.service.impl;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.ActivationConstant;
import com.example.common.constant.RedisConstant;
import com.example.common.constant.SystemConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.response.R;
import com.example.common.utils.IPUtils;
import com.example.common.utils.MobileUtils;
import com.example.common.utils.StringUtils;
import com.example.system.domain.bo.LoginBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.bo.UserBo;
import com.example.system.domain.entity.Login;
import com.example.system.domain.entity.Role;
import com.example.system.domain.entity.User;
import com.example.system.domain.vo.LoginVo;
import com.example.system.domain.vo.PageVo;
import com.example.system.mapper.LoginMapper;
import com.example.system.mapper.RoleMapper;
import com.example.system.mapper.UserMapper;
import com.example.system.service.ActivationService;
import com.example.system.service.LoginService;
import com.example.system.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> implements LoginService {
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivationService activationService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PermServiceImpl permServiceImpl;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 登录方法
     */
    @Override
    public R<Object> doLogin(LoginBo bo) {
        String account = bo.getAccount();
        if (StringUtils.isEmpty(account)) {
            return R.warn("账号不能为空");
        }
        String pwd = bo.getPassword();
        if (StringUtils.isEmpty(pwd)) {
            return R.warn("密码不能为空");
        }
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
        String ip = IPUtils.getIp(request);
        if (!bo.getAuto()) {
            //自动登录则跳过 ，否则检查验证码
            if (redisUtils.noHasKey(RedisConstant.CODE + ":" + ip)) {
                return R.warn("验证码已经过期");
            }
        }
        User user = userMapper.getUserByAccount(account);
        if (StringUtils.isEmpty(user)) {
            return R.fail("账号不存在");
        }
        if (SystemConstant.USER_STATUS_DISABLE.equals(user.getStatus())) {
            return R.fail("账号已被禁用，请联系管理员解封");
        }
        int errorNum = 0;
        String key = SystemConstant.USER_STATUS_TEMPORARILY_DISABLE + ":" + account;
        if (redisUtils.hasKey(key)) {
            errorNum = Integer.parseInt(redisUtils.get(key).toString());
        }
        if (errorNum >= SystemConstant.PASSWORD_MAX_ERROR_NUM) {
            return R.fail("此账号因密码错误次数太对，已经被暂时禁用。");
        }
        // 验证密码是否正确
        //（用户输入的密码+盐值）加密
        String sha256 = SaSecureUtil.sha256(pwd + user.getSalt());
        //真正的密码（加密的）
        String password = user.getPassword();
        if (!sha256.equals(password)) {
            long time = SystemConstant.PASSWORD_MAX_ERROR_NUM_TIME;
            redisUtils.set(key, errorNum + 1, time);
            return R.fail("密码错误");
        }
        this.checkUserRole(user.getId());
        checkLoginIsFirst(user.getId(), user.getAccount());
        Login login = new Login();
        //账号密码验证成功，添加登录记录
        login.setUsername(user.getUsername());
        login.setAccount(account);
        login.setTime(LocalDateTime.now());
        login.setIp(ip);
        String mobile = MobileUtils.getDeviceFrom(DeviceUtils.getCurrentDevice(request));
        String innerIp = IPUtils.innerIp(ip);
        login.setInnerIp(innerIp);
        login.setMobile(mobile);
        loginMapper.insertLogin(login);
        if (errorNum > 0) {
            redisUtils.del(key);
        }
        SaLoginModel saLoginModel = new SaLoginModel().setDevice(mobile);
        StpUtil.login(account, saLoginModel);
        permServiceImpl.createUserPermList(account);
        String token = StpUtil.getTokenValue();
        String tokenName = SaManager.getConfig().getTokenName();
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("tokenName", tokenName);
        map.put("username", user.getUsername());
        return R.ok("登录成功", map);
    }

    /**
     * 检查用户是否分配角色
     */
    @SneakyThrows
    private void checkUserRole(Integer userId) {
        Role role = roleMapper.getRoleByUserId(userId);
        if(StringUtils.isEmpty(role)){
            throw new Exception("该账户未分配角色，请联系管理员！");
        }
    }

    /**
     * 检查用户是否为今日第一次登录
     */
    @Async
    public void checkLoginIsFirst(Integer userId, String account) {
        LambdaQueryWrapper<Login> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Login::getAccount, account);
        String time = LocalDateTimeUtil.formatNormal(LocalDateTime.now()).replace(":", "-").split(" ")[0];
        lqw.like(Login::getTime, time);
        if (StringUtils.isEmpty(loginMapper.selectList(lqw))) {
            activationService.updateActivation(userId, ActivationConstant.LOGIN);
        }
    }

    /**
     * 注册
     */
    @Override
    @SneakyThrows
    public R<Map<String, Object>> doRegister(UserBo bo) {
        String message = userService.insertUser(bo);
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(User::getCreateTime);
        lqw.last("limit 1");

        User user = userMapper.selectOne(lqw);
        if (StringUtils.isEmpty(user)) {
            throw new Exception("注册出现未知错误");
        }
        return R.ok(message, virtualLogin(user.getId()));
    }


    /**
     * 获取登录记录
     */
    @Override
    public PageVo getPageVo(PageBo pageBo) {
        LambdaQueryWrapper<Login> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Login::getIp);
        List<Login> loginList = loginMapper.selectList(lqw);
        return new PageVo(pageBo, loginList);
    }

    /**
     * 主页回显登录记录
     */
    @Override
    public List<LoginVo> getListIndex() {
        // 主页回显个数
        int limit = 4;
        List<LoginVo> list = new ArrayList<>();
        String account = (String) StpUtil.getLoginId();
        List<LoginVo> loginList = loginMapper.getList();
        loginList = loginList.stream().filter(o -> o.getAccount().equals(account)).sorted(Comparator.comparing(LoginVo::getTime).reversed()).collect(Collectors.toList());
        for (int i = 0; i < Math.min(loginList.size(), limit); i++) {
            LoginVo loginVo = loginList.get(i);
            list.add(loginVo);
        }
        return list;
    }

    /**
     * 虚拟登录
     */
    @Override
    @SneakyThrows
    public Map<String, Object> virtualLogin(Integer id) {
        this.checkUserRole(id);
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getId, id);
        User user = userMapper.selectOne(lqw);
        if(user.getStatus().equals(SystemConstant.USER_STATUS_DISABLE)){
            throw new Exception("该账户已被禁用，请联系超级管理员！");
        }
        String ip = IPUtils.getIp(request);
        checkLoginIsFirst(user.getId(), user.getAccount());
        Login login = new Login();
        //账号密码验证成功，添加登录记录
        login.setUsername(user.getUsername());
        login.setAccount(user.getAccount());
        login.setTime(LocalDateTime.now());
        login.setIp(ip);
        String mobile = MobileUtils.getDeviceFrom(DeviceUtils.getCurrentDevice(request));
        String innerIp = IPUtils.innerIp(ip);
        login.setInnerIp(innerIp);
        login.setMobile(mobile);
        loginMapper.insertLogin(login);
        SaLoginModel saLoginModel = new SaLoginModel().setDevice(mobile);
        StpUtil.login(user.getAccount(), saLoginModel);
        permServiceImpl.createUserPermList(user.getAccount());
        String token = StpUtil.getTokenValue();
        String tokenName = SaManager.getConfig().getTokenName();
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("tokenName", tokenName);
        map.put("username", user.getUsername());
        map.put("account", user.getAccount());
        return map;
    }

}
