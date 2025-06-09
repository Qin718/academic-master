package com.example.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.RedisConstant;
import com.example.common.constant.SystemConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.response.R;
import com.example.common.utils.ClassUtils;
import com.example.common.utils.StringUtils;
import com.example.system.domain.entity.*;
import com.example.system.mapper.*;
import com.example.system.others.perm.PermInter;
import com.example.system.service.PermService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermServiceImpl extends ServiceImpl<PermMapper, Perm> implements PermService {
    @Autowired
    private PermMapper permMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserPermMapper userPermMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RolePermMapper rolePermMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 获取权限列表-不分页
     */
    @Override
    public List<Perm> getList() {
        return permMapper.selectList(new LambdaQueryWrapper<>());
    }

    /**
     * 初始化权限列表
     */
    @Override
    @SneakyThrows
    public void createPermRedis() {
        List<Perm> perms = new ArrayList<>();
        Role role = roleMapper.selectList(new LambdaQueryWrapper<>()).stream().max(Comparator.comparing(Role::getAccess)).orElse(null);
        if (role == null) {
            return;
        }
        Integer jb = role.getAccess();
        String packagePath = SystemConstant.CONTROLLER_PATH;
        // 获取包下所有的类
        List<Class<?>> classes = ClassUtils.getClzFromPkg(packagePath);

//        List<Class<?>> classes = getClasses(packagePath);
        classes.stream().peek(c -> {
            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                PermInter inter = method.getDeclaredAnnotation(PermInter.class);
                if (inter != null) {
                    Perm perm = new Perm();
                    perm.setJsjb(Integer.parseInt(inter.jsjb()));
                    perm.setName(inter.name());
                    perm.setPerm(inter.perm());
                    if (perm.getJsjb() == 0) {
                        perm.setJsjb(jb);
                    }
                    perms.add(perm);
                }
            }

        }).collect(Collectors.toList());

        List<Perm> list = permMapper.selectList(new LambdaQueryWrapper<>());
        List<Perm> listNoId = list.stream().peek(o -> o.setId(null)).collect(Collectors.toList());

        List<Perm> insert = perms.stream().filter(o -> !listNoId.contains(o)).collect(Collectors.toList());
        List<String> delete = listNoId.stream().filter(o -> !perms.contains(o)).map(Perm::getPerm).collect(Collectors.toList());

        if (StringUtils.isNotEmpty(delete)) {
            LambdaQueryWrapper<Perm> lqw = new LambdaQueryWrapper<>();
            lqw.in(Perm::getPerm, delete);
            permMapper.delete(lqw);
            LambdaQueryWrapper<RolePerm> lqw1 = new LambdaQueryWrapper<>();
            List<Integer> deleteIds = list.stream().filter(o -> delete.contains(o.getPerm())).map(Perm::getId).collect(Collectors.toList());
            lqw1.in(RolePerm::getPermId, deleteIds);
            rolePermMapper.delete(lqw1);
        }
        if (StringUtils.isNotEmpty(insert)) {
            permMapper.insertList(insert);
        }

    }

    /**
     * 获取角色的权限列表
     */
    @Override
    public List<String> getListByRole(Integer roleId) {
        Role role = roleMapper.selectById(roleId);
        LambdaQueryWrapper<Perm> lqw = new LambdaQueryWrapper<>();
        lqw.ge(Perm::getJsjb, role.getAccess());
        List<String> list = permMapper.selectList(lqw).stream().map(Perm::getName).collect(Collectors.toList());

        LambdaQueryWrapper<RolePerm> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(RolePerm::getRoleId, roleId);
        List<RolePerm> rolePerms = rolePermMapper.selectList(lqw1);

        List<RolePerm> insert = rolePerms.stream().filter(o -> "INSERT".equals(o.getRelationship())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(insert)) {
            List<Integer> permId = insert.stream().map(RolePerm::getPermId).collect(Collectors.toList());
            LambdaQueryWrapper<Perm> lqw2 = new LambdaQueryWrapper<>();
            lqw2.in(Perm::getId, permId);
            List<String> list1 = permMapper.selectList(lqw2).stream().map(Perm::getName).collect(Collectors.toList());
            list.addAll(list1);
        }

        List<RolePerm> delete = rolePerms.stream().filter(o -> "DELETE".equals(o.getRelationship())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delete)) {
            List<Integer> permId = delete.stream().map(RolePerm::getPermId).collect(Collectors.toList());
            LambdaQueryWrapper<Perm> lqw2 = new LambdaQueryWrapper<>();
            lqw2.in(Perm::getId, permId);
            List<String> list1 = permMapper.selectList(lqw2).stream().map(Perm::getName).collect(Collectors.toList());
            list = list.stream().filter(o -> !list1.contains(o)).collect(Collectors.toList());
        }


        return list;
    }

    /**
     * 将账号和权限关联起来
     * 关联在登录方法中，用户登录 生成对应的权限列表
     */
    @Async
    @Override
    public void createUserPermList(String account) {
        List<Perm> perms = permMapper.selectList(new LambdaQueryWrapper<>());
        User user = userMapper.getUserByAccount(account);
        Role role = roleMapper.getRoleByAccount(account);
        Integer access = role.getAccess();
        List<Perm> permList = perms.stream().filter(p -> p.getJsjb() >= access).collect(Collectors.toList());
        //根据角色初步生成权限
        List<UserPerm> list = permList.stream().map(o -> {
            UserPerm userPerm = new UserPerm();
            userPerm.setUserId(user.getId());
            userPerm.setPermId(o.getId());
            return userPerm;
        }).collect(Collectors.toList());
        //根据sys_role_perm生成权限
        List<RolePerm> rolePerms = rolePermMapper.selectList(new LambdaQueryWrapper<RolePerm>().eq(RolePerm::getRoleId, role.getId()));
        if (StringUtils.isNotEmpty(rolePerms)) {
            List<RolePerm> insert = rolePerms.stream().filter(o -> "INSERT".equals(o.getRelationship())).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(insert)) {
                List<Integer> ids = insert.stream().map(RolePerm::getPermId).collect(Collectors.toList());
                List<UserPerm> list1 = perms.stream().
                        filter(o -> ids.contains(o.getId()))
                        .map(o -> {
                            UserPerm userPerm = new UserPerm();
                            userPerm.setUserId(user.getId());
                            userPerm.setPermId(o.getId());
                            return userPerm;
                        }).collect(Collectors.toList());
                list.addAll(list1);
            }
            List<RolePerm> delete = rolePerms.stream().filter(o -> "DELETE".equals(o.getRelationship())).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(delete)) {
                List<Integer> ids = delete.stream().map(RolePerm::getPermId).collect(Collectors.toList());
                list = list.stream().filter(o -> !ids.contains(o.getPermId())).collect(Collectors.toList());
            }
        }
        //此时list为该账号所拥有的权限

        List<UserPerm> list1 = userPermMapper.selectList(new LambdaQueryWrapper<UserPerm>().eq(UserPerm::getUserId, user.getId()));

        List<Integer> listPermId = list.stream().map(UserPerm::getPermId).collect(Collectors.toList());
        List<Integer> list1PermId = list1.stream().map(UserPerm::getPermId).collect(Collectors.toList());

        List<Integer> insertId = listPermId.stream().filter(o -> !list1PermId.contains(o)).collect(Collectors.toList());
        List<Integer> deleteId = list1PermId.stream().filter(o -> !listPermId.contains(o)).collect(Collectors.toList());

        if (StringUtils.isNotEmpty(insertId)) {
            List<UserPerm> insert = list.stream().filter(o -> insertId.contains(o.getPermId())).collect(Collectors.toList());
            userPermMapper.insertList(insert);
        }
        if (StringUtils.isNotEmpty(deleteId)) {
            LambdaQueryWrapper<UserPerm> lqw = new LambdaQueryWrapper<>();
            lqw.eq(UserPerm::getUserId, user.getId());
            lqw.in(UserPerm::getPermId, deleteId);
            userPermMapper.delete(lqw);
        }
        this.createUserPermRedis(account);
    }

    /**
     * 更新角色权限表
     */
    @Override
    public R<String> changeRolePerm(List<String> newPerm, List<String> oldPerm, Integer roleId) {
        List<String> insert_name = newPerm.stream().filter(o -> !oldPerm.contains(o)).collect(Collectors.toList());
        List<String> delete_name = oldPerm.stream().filter(o -> !newPerm.contains(o)).collect(Collectors.toList());

        List<Perm> perms = permMapper.selectList(new LambdaQueryWrapper<>());

        if (StringUtils.isNotEmpty(insert_name)) {
            List<RolePerm> insert_list = perms.stream().filter(o -> insert_name.contains(o.getName()))
                    .map(o -> {
                        RolePerm rolePerm = new RolePerm();
                        rolePerm.setRoleId(roleId);
                        rolePerm.setPermId(o.getId());
                        rolePerm.setRelationship("INSERT");
                        return rolePerm;
                    }).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(insert_list)) {
                rolePermMapper.deleteList(insert_list);
                rolePermMapper.insertList(insert_list);
            }
        }
        if (StringUtils.isNotEmpty(delete_name)) {
            List<RolePerm> delete_list = perms.stream().filter(o -> delete_name.contains(o.getName()))
                    .map(o -> {
                        RolePerm rolePerm = new RolePerm();
                        rolePerm.setRoleId(roleId);
                        rolePerm.setPermId(o.getId());
                        rolePerm.setRelationship("DELETE");
                        return rolePerm;
                    }).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(delete_list)) {
                rolePermMapper.deleteList(delete_list);
                rolePermMapper.insertList(delete_list);
            }
        }
        //搜索该角色所有账号，删除其缓存
        LambdaQueryWrapper<UserRole> lqwUserRole = new LambdaQueryWrapper<>();
        lqwUserRole.eq(UserRole::getRoleId,roleId);
        List<UserRole> userRoleList = userRoleMapper.selectList(lqwUserRole);
        if(StringUtils.isNotEmpty(userRoleList)) {
            List<Integer> userIds = userRoleList.stream().map(UserRole::getUserId).collect(Collectors.toList());
            LambdaQueryWrapper<User> lqwUser = new LambdaQueryWrapper<>();
            lqwUser.in(User::getId, userIds);
            List<User> userList = userMapper.selectList(lqwUser);
            if (StringUtils.isNotEmpty(userList)) {
                List<String> accounts = userList.stream().map(User::getAccount).collect(Collectors.toList());
                accounts.stream().peek(o -> {
                    String key = RedisConstant.GET_LIST_PERM + o;
                    if (redisUtils.hasKey(key)) {
                        this.createUserPermList(o);
                    }
                }).collect(Collectors.toList());
            }
        }
        return R.ok("操作成功");
    }

    /**
     * 生成某一账号权限列表的缓存
     */
    public void createUserPermRedis(String account) {
        User user = userMapper.getUserByAccount(account);
        List<Perm> perms = permMapper.selectList(new LambdaQueryWrapper<>());
        List<UserPerm> list = userPermMapper.selectList(new LambdaQueryWrapper<UserPerm>().eq(UserPerm::getUserId, user.getId()));
        List<Integer> permIds = list.stream().map(UserPerm::getPermId).collect(Collectors.toList());
        List<String> value = perms.stream().filter(o -> permIds.contains(o.getId())).map(Perm::getPerm).collect(Collectors.toList());
        String key = RedisConstant.GET_LIST_PERM + account;
        long time = RedisConstant.GET_LIST_PERM_TIME;
        redisUtils.set(key, value, time);
    }
}
