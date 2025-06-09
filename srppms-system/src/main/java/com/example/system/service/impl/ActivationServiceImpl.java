package com.example.system.service.impl;


import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.ActivationConstant;
import com.example.common.utils.StringUtils;
import com.example.common.webSocket.SendSocket;
import com.example.common.webSocket.WebSocket;
import com.example.system.domain.entity.Activation;
import com.example.system.domain.entity.Role;
import com.example.system.domain.entity.User;
import com.example.system.domain.entity.UserRole;
import com.example.system.mapper.ActivationMapper;
import com.example.system.mapper.RoleMapper;
import com.example.system.mapper.UserMapper;
import com.example.system.mapper.UserRoleMapper;
import com.example.system.service.ActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ActivationServiceImpl extends ServiceImpl<ActivationMapper, Activation> implements ActivationService {
    @Autowired
    private ActivationMapper activationMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 更新活跃度
     */
    @Async
    @Override
    public void updateActivation(Integer userId, String type) {
        LambdaQueryWrapper<Activation> lqwActivation = new LambdaQueryWrapper<>();
        lqwActivation.eq(Activation::getUserId, userId);
        Activation activation = activationMapper.selectOne(lqwActivation);
        if (StringUtils.isEmpty(activation)) {
            activation = new Activation();
            activation.setUserId(userId);
            activationMapper.insertActivation(activation);
        }
        //登录活跃度
        double login = activation.getLogin();
        if (ActivationConstant.LOGIN.equals(type)) {
            login += ActivationConstant.ACTIVATION_LOGIN;
            activation.setLogin(login);
        }
        //创建项目活跃度
        double addItem = activation.getAddItem();
        if (ActivationConstant.ADD_ITEM.equals(type)) {
            addItem += ActivationConstant.ACTIVATION_ADD_ITEM;
            activation.setAddItem(addItem);
        }
        //审核项目活跃度
        double process = activation.getProcess();
        if (ActivationConstant.PROCESS.equals(type)) {
            process += ActivationConstant.ACTIVATION_PROCESS;
            activation.setProcess(process);
        }
        //评分活跃度
        double score = activation.getScoreProject();
        if (ActivationConstant.SCORE.equals(type)) {
            score += ActivationConstant.ACTIVATION_SCORE;
            activation.setScoreProject(score);
        }

        //总活跃度
        double all = login + addItem + process + score;
        activation.setAllActivation(all);
        activationMapper.updateById(activation);

        //获取当前用户权限
        Role role = roleMapper.getRoleByUserId(userId);
        int access = role.getAccess();
        //寻找上一级权限
        List<Role> roles = roleMapper.selectList(new LambdaQueryWrapper<>()).stream().filter(o -> "YES".equals(o.getByAct()) && o.getAccess() < access).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(roles)) {
            role = roles.stream().min(Comparator.comparing(Role::getActivation)).orElse(null);
            //活跃度足够，升级权限
            if (role != null && all >= role.getActivation()) {

                LambdaQueryWrapper<UserRole> lqwUserRole = new LambdaQueryWrapper<>();
                lqwUserRole.eq(UserRole::getUserId, userId);
                UserRole userRole = userRoleMapper.selectOne(lqwUserRole);
                userRole.setRoleId(role.getId());
                userRoleMapper.updateById(userRole);
                // 更新用户登录信息
                LambdaQueryWrapper<User> lqwUser = new LambdaQueryWrapper<>();
                lqwUser.eq(User::getId,userId);
                User user = userMapper.selectOne(lqwUser);
                String token = StpUtil.getTokenValueByLoginId(user.getAccount());
                String tokenName = SaManager.getConfig().getTokenName();

                Map<String, Object> map = new HashMap<>();
                map.put("token", token);
                map.put("tokenName", tokenName);
                map.put("username", user.getUsername());
                map.put("account", user.getAccount());

                //发送WebSocket
                SendSocket socket = new SendSocket();
                socket.setType("userInfo");
                socket.setMessage("该用户权限已升级：" + role.getRoleName());
                socket.setData(map);

                WebSocket webSocket = new WebSocket();
                webSocket.sendOneMessage(user.getAccount(), socket);
            }
        }
    }
}
