package com.example.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.RedisConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.response.R;
import com.example.common.utils.StringUtils;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.bo.RoleBo;
import com.example.system.domain.entity.Role;
import com.example.system.domain.entity.RoleMenu;
import com.example.system.domain.entity.RolePerm;
import com.example.system.domain.entity.UserRole;
import com.example.system.domain.vo.PageVo;
import com.example.system.mapper.*;
import com.example.system.service.RoleService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private RolePermMapper rolePermMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 获取角色列表-分页
     */
    @Override
    public PageVo getPageVo(PageBo pageBo) {
        List<Role> list = redisUtils.get(RedisConstant.GET_LIST_ROLE, Role.class);
        return new PageVo(pageBo, list);
    }

    /**
     * 获取角色列表-缓存搜索
     */
    @Override
    public List<Role> getListRoleKind() {
        List<Role> roles = redisUtils.get(RedisConstant.GET_LIST_ROLE, Role.class);
        roles = roles.stream().map(o -> {
            Role role = new Role();
            role.setId(o.getId());
            role.setRoleName(o.getRoleName());
            role.setAccess(o.getAccess());
            return role;
        }).collect(Collectors.toList());
        return roles;
    }

    /**
     * 创建角色列表缓存
     */
    @Override
    public void createRoleRedis() {
        List<Role> list = roleMapper.getList();
        String key = RedisConstant.GET_LIST_ROLE;
        long time = RedisConstant.GET_LIST_ROLE_TIME;
        redisUtils.set(key, list, time);
    }

    /**
     * 新增角色
     */
    @Override
    @SneakyThrows
    public String insertRole(RoleBo bo) {
        this.checkRoleBeforeInsert(bo);

        String account = (String) StpUtil.getLoginId();
        Role role = roleMapper.getRoleByAccount(account);
        if (bo.getAccess() < role.getAccess()) {
            LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Role::getAccess, bo.getAccess());
            Role role1 = roleMapper.selectOne(lqw);
            //低权限创建高权限角色
            throw new Exception("\"" + role.getRoleName() + "\"无法创建\"" + role1.getRoleName() + "\"权限的角色");
        }


        int userId = userMapper.getUserByAccount(account).getId();
        bo.setCreateBy(userId);
        bo.setCreateTime(LocalDateTime.now());
        roleMapper.insertBo(bo);
        createRoleRedis();
        return "添加成功";
    }

    /**
     * 搜索角色列表-分页
     */
    @Override
    public R<PageVo> getPageVoSearch(RoleBo roleBo, PageBo pageBo) {
        List<Role> roles = redisUtils.get(RedisConstant.GET_LIST_ROLE, Role.class);

        String roleName = roleBo.getRoleName();
        if(StringUtils.isNotEmpty(roleName)){
            roles = roles.stream().filter(o -> (StringUtils.isNotEmpty(o.getRoleName()) && o.getRoleName().contains(roleName))).collect(Collectors.toList());
        }

        String remark = roleBo.getRemark();
        if(StringUtils.isNotEmpty(remark)){
            roles = roles.stream().filter(o -> (StringUtils.isNotEmpty(o.getRemark()) && o.getRemark().contains(remark))).collect(Collectors.toList());
        }

        String createByName = roleBo.getCreateByName();
        if(StringUtils.isNotEmpty(createByName)){
            roles = roles.stream().filter(o -> (StringUtils.isNotEmpty(o.getCreateByName()) && o.getCreateByName().contains(createByName))).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(roles)) {
            return R.ok(new PageVo(pageBo, roles));
        }
        return R.info("没有符合条件的数据", new PageVo());
    }

    /**
     * 修改角色
     */
    @Override
    public String updateRole(RoleBo roleBo) {
        this.checkRoleBeforeUpdate(roleBo);
        roleMapper.updateById(roleBo);
        createRoleRedis();
        return "修改成功";
    }

    /**
     * 删除角色
     */
    @Override
    @SneakyThrows
    public String deleteRole(List<Integer> list) {
        //sys_role_menu
        LambdaQueryWrapper<RoleMenu> lqwRoleMenu = new LambdaQueryWrapper<>();
        lqwRoleMenu.in(RoleMenu::getRoleId, list);
        roleMenuMapper.delete(lqwRoleMenu);

        //sys_role_perm
        LambdaQueryWrapper<RolePerm> lqwRolePerm = new LambdaQueryWrapper<>();
        lqwRolePerm.in(RolePerm::getRoleId, list);
        rolePermMapper.delete(lqwRolePerm);

        //sys_user_role 需要降低用户的权限，而不是删除
        LambdaQueryWrapper<UserRole> lqwUserRole = new LambdaQueryWrapper<>();
        lqwUserRole.in(UserRole::getRoleId, list);
        userRoleMapper.delete(lqwUserRole);

        //sys_role
        LambdaQueryWrapper<Role> lqwRole = new LambdaQueryWrapper<>();
        lqwRole.in(Role::getId, list);
        roleMapper.delete(lqwRole);

        createRoleRedis();
        return "删除成功";
    }

    /**
     * 修改角色前，进行数据检查
     */
    @SneakyThrows
    private void checkRoleBeforeUpdate(Role role) {
        this.checkRole(role);

        List<Role> roles = redisUtils.get(RedisConstant.GET_LIST_ROLE, Role.class);
        if (roles.stream().anyMatch(o -> o.getRoleName().equals(role.getRoleName()) && !o.getId().equals(role.getId()))) {
            throw new Exception("数据检查出现异常：权限名称已存在");
        }
        if (roles.stream().anyMatch(o -> o.getAccess().equals(role.getAccess()) && !o.getId().equals(role.getId()))) {
            throw new Exception("数据检查出现异常：权限级别已存在");
        }
    }

    @SneakyThrows
    private void checkRole(Role role) {
        if (StringUtils.isEmpty(role.getRoleName())) {
            throw new Exception("数据检查出现异常：角色名称为空");
        }
        if (StringUtils.isEmpty(role.getAccess() + "")) {
            throw new Exception("数据检查出现异常：角色级别为空");
        }
        if (role.getAccess() <= 0) {
            throw new Exception("数据检查出现异常：角色权限不能小于等于0");
        }

        List<Role> roles = redisUtils.get(RedisConstant.GET_LIST_ROLE, Role.class);
        if ("YES".equals(role.getByAct())) {
            if (roles.stream().anyMatch(o -> o.getAccess() < role.getAccess() && o.getActivation() < role.getActivation())) {
                throw new Exception("数据检查出现异常：存在权限高于添加权限，且所需活跃度低于添加权限的活跃度！\n建议降低所需活跃度。");
            }
            if (roles.stream().anyMatch(o -> o.getAccess() > role.getAccess() && o.getActivation() > role.getActivation())) {
                throw new Exception("数据检查出现异常：存在权限低于添加权限，且所需活跃度高于添加权限的活跃度！\n建议提高所需活跃度。");
            }
        }
    }

    /**
     * 添加角色前，进行数据检查
     */
    @SneakyThrows
    private void checkRoleBeforeInsert(Role role) {
        this.checkRole(role);

        List<Role> roles = redisUtils.get(RedisConstant.GET_LIST_ROLE, Role.class);
        if (roles.stream().anyMatch(o -> o.getRoleName().equals(role.getRoleName()))) {
            throw new Exception("数据检查出现异常：权限名称已存在");
        }
        if (roles.stream().anyMatch(o -> Objects.equals(o.getAccess(), role.getAccess()))) {
            throw new Exception("数据检查出现异常：权限级别已存在");
        }
    }

}
