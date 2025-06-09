package com.example.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.RedisConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.response.R;
import com.example.common.utils.StringUtils;
import com.example.system.domain.bo.MenuBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.entity.Menu;
import com.example.system.domain.entity.Role;
import com.example.system.domain.entity.RoleMenu;
import com.example.system.domain.vo.MenuVo;
import com.example.system.domain.vo.PageVo;
import com.example.system.mapper.MenuMapper;
import com.example.system.mapper.RoleMapper;
import com.example.system.mapper.RoleMenuMapper;
import com.example.system.others.utils.CheckUtils;
import com.example.system.service.MenuService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    List<Integer> updateParentIds = new ArrayList<>();
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * 侧边栏动态获取菜单，依据用户权限
     */
    @Override
    public List<MenuVo> getAsideMenus() {
        String account = (String) StpUtil.getLoginId();
        //获取用户的权限（既角色信息）
        Role role = roleMapper.getRoleByAccount(account);
        //角色Id
        int roleId = role.getId();
        int access = role.getAccess();
        List<Menu> menus;
        //查询缓存是否存在
        String key = RedisConstant.GET_LIST_MENU_ASIDE + ":" + role.getAccess();
        if (redisUtils.hasKey(key)) {
            //缓存存在
            menus = redisUtils.get(key, Menu.class);
        } else {
            menus = getMenuList().stream().filter(o -> o.getAccess() >= access).collect(Collectors.toList());

            LambdaQueryWrapper<RoleMenu> lqw = new LambdaQueryWrapper<>();
            lqw.eq(RoleMenu::getRoleId, roleId);
            List<RoleMenu> rmList = roleMenuMapper.selectList(lqw);
            if (StringUtils.isNotEmpty(rmList)) {
                List<Integer> deleteList = rmList.stream().filter(o -> "DELETE".equals(o.getRelationship())).collect(Collectors.toList()).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(deleteList)) {
                    LambdaQueryWrapper<Menu> lqw1 = new LambdaQueryWrapper<>();
                    lqw1.in(Menu::getId, deleteList);
                    List<Menu> deletes = menuMapper.selectList(lqw1);
                    // menus和deletes的差集（menus - deletes）
                    menus = menus.stream().filter(item -> !deletes.contains(item)).collect(Collectors.toList());
                }
                List<Integer> insertList = rmList.stream().filter(o -> "INSERT".equals(o.getRelationship())).collect(Collectors.toList()).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(insertList)) {
                    LambdaQueryWrapper<Menu> lqw1 = new LambdaQueryWrapper<>();
                    lqw1.in(Menu::getId, insertList);
                    List<Menu> inserts = menuMapper.selectList(lqw1);
                    // menus和inserts并集（去重）
                    menus.addAll(inserts);
                    menus = menus.stream().distinct().collect(Collectors.toList());
                }
            }
            for (Menu i : menus) {
                List<Menu> list = menus.stream().filter(menu -> Objects.equals(menu.getParentId(), i.getId())
//                            && menu.getAccess() <= i.getAccess()
                                && (1 == menu.getType())
                ).collect(Collectors.toList());
                if (!list.isEmpty()) {
                    list.sort(Comparator.comparing(Menu::getOrderNum));
                    i.setChildren(list);
                }
            }
            menus = menus.stream().filter(o -> o.getParentId() == 0).collect(Collectors.toList());
            long time = RedisConstant.GET_LIST_MENU_ASIDE_TIME;
            redisUtils.set(key, menus, time);
        }

        return StringUtils.copyList(menus, MenuVo.class);
    }

    /**
     * 获取全部菜单
     * 采用Parent-Children模式
     */
    @Override
    public PageVo getPageVo(PageBo pageBo) {
        List<Menu> menus = getMenuRedis();
        return new PageVo(pageBo, menus);
    }

    /**
     * 获取父菜单
     */
    @Override
    public List<Menu> getListMenuParent() {
        return redisUtils.get(RedisConstant.GET_LIST_MENU_PARENT, Menu.class);
    }

    /**
     * 更新菜单
     */
    @Override
    public String updateMenu(MenuBo menuBo) {
        this.checkMenuBeforeUpdate(menuBo);
        updateParentIds.add(menuBo.getParentId());
        Integer parentId = menuMapper.selectById(menuBo.getId()).getParentId();
        if (!parentId.equals(menuBo.getParentId())) {
            updateParentIds.add(parentId);
        }
        menuMapper.updateById(menuBo);
        this.updateMenu();
        return "修改成功";
    }

    /**
     * 增加菜单
     */
    @Override
    @SneakyThrows
    public String insertMenu(MenuBo menu) {
        this.checkMenuBeforeInsert(menu);
        if (StringUtils.isEmpty(menu.getOrderNum())) {
            menu.setOrderNum(0);
        }
        if (StringUtils.isEmpty(menu.getParentId())) {
            menu.setParentId(0);
        }
        updateParentIds.add(menu.getParentId());
        menuMapper.insertMenu(menu);
        this.updateMenu();
        return "添加成功";

    }

    /**
     * 删除菜单
     */
    @Override
    public String deleteMenu(List<Integer> list) {
        //修改子菜单的parentId,为0
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        lqw.in(Menu::getParentId, list);
        List<Menu> children = menuMapper.selectList(lqw);
        if (StringUtils.isNotEmpty(children)) {
            children = children.stream().peek(o -> {
                o.setParentId(0);
                o.setOrderNum(1000);
            }).collect(Collectors.toList());
            this.updateParentIds.add(0);
            menuMapper.updateMenuList(children);
        }

        //删除菜单
        lqw = new LambdaQueryWrapper<>();
        lqw.in(Menu::getId, list);
        List<Menu> menus = menuMapper.selectList(lqw);
        //记录父菜单
        menus.stream().peek(menu -> this.updateParentIds.add(menu.getParentId())).collect(Collectors.toList());
        //删除菜单
        menuMapper.delete(lqw);

        //sys_role_menu
        LambdaQueryWrapper<RoleMenu> lqwRoleMenu = new LambdaQueryWrapper<>();
        lqwRoleMenu.in(RoleMenu::getMenuId, list);
        roleMenuMapper.delete(lqwRoleMenu);

        this.updateMenu();
        return "删除成功";
    }

    /**
     * 获取菜单列表
     */
    @Override
    public List<Menu> getMenuRedis() {
        return redisUtils.get(RedisConstant.GET_LIST_MENU, Menu.class);
    }

    /**
     * 模糊查询菜单
     */
    @Override
    public R<PageVo> getPageVoSearchByName(MenuBo menuBo, PageBo pageBo) {
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<>());
        if (StringUtils.isNotEmpty(menuBo.getName())) {
            menuList = menuList.stream().filter(o -> o.getName().contains(menuBo.getName())).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(menuBo.getUrl())) {
            menuList = menuList.stream().filter(o -> o.getUrl().contains(menuBo.getUrl())).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(menuBo.getParentId())) {
            menuList = menuList.stream().filter(o -> o.getParentId().equals(menuBo.getParentId())).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(menuBo.getType())) {
            menuList = menuList.stream().filter(o -> o.getType().equals(menuBo.getType())).collect(Collectors.toList());
        }
        if (StringUtils.isEmpty(menuBo.getName()) && StringUtils.isEmpty(menuBo.getUrl()) && StringUtils.isEmpty(menuBo.getParentId()) && StringUtils.isEmpty(menuBo.getType())) {
            menuList = this.getMenuRedis();
        }
        if (StringUtils.isNotEmpty(menuList)) {
            return R.ok(new PageVo(pageBo, menuList));
        }
        return R.info("没有符合条件的数据", new PageVo());
    }

    /**
     * 获取所有菜单列表-list<Menu>格式
     */
    private List<Menu> getMenuList() {
        return menuMapper.getMenuList();
    }

    /**
     * 创建父菜单缓存
     */
    @Override
    public void createMenuParentRedis() {
        List<Menu> menus = getMenuList().stream().filter(o -> o.getParentId() == 0).collect(Collectors.toList());
        long time = RedisConstant.GET_LIST_MENU_PARENT_TIME;
        String key = RedisConstant.GET_LIST_MENU_PARENT;
        redisUtils.set(key, menus, time);
    }

    /**
     * 创建菜单列表缓存
     */
    @Override
    public void createMenuRedis() {
        List<Menu> menus = getMenuParentChildren();
        long time = RedisConstant.GET_LIST_MENU_TIME;
        String key = RedisConstant.GET_LIST_MENU;
        redisUtils.set(key, menus, time);
    }

    /**
     * 根据角色获取菜单列表
     */
    @Override
    public List<Integer> getListByRole(Integer roleId) {
        Role role = roleMapper.getRoleByUserId(roleId);
        int access = role.getAccess();
        List<Menu> menus = getMenuList().stream().filter(o -> o.getAccess() >= access).collect(Collectors.toList());
        List<Integer> list = menus.stream().map(Menu::getId).collect(Collectors.toList());

        LambdaQueryWrapper<RoleMenu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RoleMenu::getRoleId, roleId);
        List<RoleMenu> rmList = roleMenuMapper.selectList(lqw);
        List<Integer> insertList = rmList.stream().filter(o -> "INSERT".equals(o.getRelationship())).collect(Collectors.toList()).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        List<Integer> deleteList = rmList.stream().filter(o -> "DELETE".equals(o.getRelationship())).collect(Collectors.toList()).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());


        // list和deleteList的差集（list - deleteList）
        list = list.stream().filter(item -> !deleteList.contains(item)).collect(Collectors.toList());
        // list和insertList并集（去重）
        list.addAll(insertList);
        list = list.stream().distinct().collect(Collectors.toList());


        return list;
    }

    /**
     * 更新角色菜单表
     */
    @Override
    public R<String> changeRoleMenu(List<Integer> newMenu, List<Integer> oldMenu, Integer roleId) {
        Role role = roleMapper.selectById(roleId);
        List<Integer> insertList = newMenu.stream().filter(item -> !oldMenu.contains(item)).collect(Collectors.toList());
        List<Integer> deleteList = oldMenu.stream().filter(item -> !newMenu.contains(item)).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(insertList)) {
            List<RoleMenu> list = new ArrayList<>();
            insertList.forEach(id -> {
                if (id != null) {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(id);
                    roleMenu.setRelationship("INSERT");
                    roleMenu.setAccess(role.getAccess());
                    list.add(roleMenu);
                }
            });
            roleMenuMapper.deleteList(list);
            roleMenuMapper.insertList(list);
        }
        if (StringUtils.isNotEmpty(deleteList)) {
            List<RoleMenu> list = new ArrayList<>();
            deleteList.forEach(id -> {
                if (id != null) {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(id);
                    roleMenu.setRelationship("DELETE");
                    roleMenu.setAccess(role.getAccess());
                    list.add(roleMenu);
                }
            });
            roleMenuMapper.deleteList(list);
            roleMenuMapper.insertList(list);
        }
        checkRoleMenu();

        //删除角色的菜单缓存
        String key = RedisConstant.GET_LIST_MENU_ASIDE + ":" + roleId;
        redisUtils.del(key);
        return R.ok("操作成功");
    }

    @Override
    public List<MenuVo> getButton() {
        List<Menu> menuList = this.getMenuList();
        List<Menu> list = menuList.stream().filter(o -> o.getType().equals(2)).peek(o -> {
            List<Menu> parent = menuList.stream().filter(menu -> menu.getId().equals(o.getParentId())).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(parent) && parent.size() == 1) {
                String url = parent.get(0).getUrl() + o.getUrl();
                url = url.replace("/", ":");
                o.setUrl(url);
            }
        }).collect(Collectors.toList());


        return StringUtils.copyList(list, MenuVo.class);
    }

    /**
     * 获取所有的菜单
     */
    public List<Menu> getMenuParentChildren() {
        List<Menu> menus = getMenuList();
        boolean hasChildren = true;
        while (hasChildren) {
            hasChildren = false;
            for (Menu i : menus) {
                List<Menu> list = menus.stream().filter(menu -> Objects.equals(menu.getParentId(), i.getId())).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(list)) {
                    list.sort(Comparator.comparing(Menu::getOrderNum));
                    List<Menu> children = i.getChildren();
                    i.setChildren(list);
                    if (StringUtils.isNotEmpty(children) && !children.equals(list)) {
                        hasChildren = true;
                    }
                }
            }
        }
        menus = menus.stream().filter(o -> o.getParentId() == 0).collect(Collectors.toList());
        return menus;
    }

    /**
     * 更新Menu表的缓存
     */
    @Async
    public void updateMenuRedis() {
        Set<Object> keys = redisTemplate.keys(RedisConstant.GET_LIST_MENU + "*");
        if (keys != null) {
            for (Object key : keys) {
                redisUtils.del(String.valueOf(key));
            }
        }
        createMenuRedis();
        createMenuParentRedis();
    }

    /**
     * 通用检查菜单
     */
    @SneakyThrows
    private void checkMenu(Menu menu) {
        if (StringUtils.isNotEmpty(menu.getParentId()) && menu.getParentId().equals(1)) {
            throw new Exception("数据检查出现异常：不能将首页作为父菜单");
        }
        if (StringUtils.isEmpty(menu.getName())) {
            throw new Exception("数据检查出现异常：菜单名称不能为空");
        }
        String url = menu.getUrl();
        if (StringUtils.isEmpty(menu.getUrl())) {
            throw new Exception("数据检查出现异常：菜单url不能为空");
        }
        if (!url.startsWith("/")) {
            throw new Exception("数据检查出现异常：菜单url格式不正确");
        }
        url = url.replaceFirst("/", "");
        if (StringUtils.isEmpty(menu.getUrl())) {
            throw new Exception("数据检查出现异常：菜单url不能为'/'");
        }
        if (CheckUtils.checkContainDigit(url)) {
            throw new Exception("数据检查出现异常：菜单url包含数字");
        }
        if (CheckUtils.checkContainSpecialChar(url)) {
            throw new Exception("数据检查出现异常：菜单url包含特殊字符");
        }
        if (CheckUtils.checkContainChineseCharacters(url)) {
            throw new Exception("数据检查出现异常：菜单url包含汉字");
        }
        if (StringUtils.isEmpty(menu.getType())) {
            throw new Exception("数据检查出现异常：菜单类型不能为空");
        }
        if (StringUtils.isEmpty(menu.getAccess())) {
            throw new Exception("数据检查出现异常：访问权限不能为空");
        }
        if (StringUtils.isEmpty(menu.getIcon())) {
            throw new Exception("数据检查出现异常：菜单图标不能为空");
        }
        if (StringUtils.isEmpty(menu.getAccess())) {
            throw new Exception("数据检查出现异常：菜单权限不能为空");
        }
    }

    /**
     * 添加菜单前，检查菜单
     */
    @SneakyThrows
    private void checkMenuBeforeInsert(Menu menu) {
        this.checkMenu(menu);
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Menu::getUrl, menu.getUrl());
        if(menu.getType().equals(2)){
            //按钮不直接检验url,而是校验同一菜单下是否存在相同url的按钮
            lqw.eq(Menu::getParentId,menu.getParentId());
        }
        boolean exists = menuMapper.exists(lqw);
        if (exists) {
            throw new Exception("菜单已存在,添加失败!");
        }
    }

    /**
     * 修改菜单前，检查菜单
     */
    @SneakyThrows
    private void checkMenuBeforeUpdate(Menu menu) {
        this.checkMenu(menu);
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Menu::getUrl, menu.getUrl());
        lqw.ne(Menu::getId, menu.getId());
        if(menu.getType().equals(2)){
            //按钮不直接检验url,而是校验同一菜单下是否存在相同url的按钮
            lqw.eq(Menu::getParentId,menu.getParentId());
        }
        boolean exists = menuMapper.exists(lqw);
        if (exists) {
            throw new Exception("菜单已存在,修改失败!");
        }
    }

    /**
     * 新增，修改，删除之后检查菜单表
     */
    private void updateMenu() {
        //更新菜单表Id
        this.updateMenuId();
        //更新菜单列表的排序
        this.updateMenuOrderNum();
        //更新路由
        this.updateComponent();
        //更新按钮的权限
        this.updateButton();
        //更新Menu表的缓存
        this.updateMenuRedis();
    }

    /**
     * 更新菜单表Id
     */
    private void updateMenuId() {
        List<Menu> menuList = getMenuList();
        List<Menu> list = menuList.stream().sorted(Comparator.comparing(Menu::getParentId).thenComparing(Menu::getOrderNum)).collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setNewMenuId(i + 1);
        }
        List<Menu> list1 = list;
        list = list.stream().peek(o -> {
            List<Menu> list2 = list1.stream().filter(f -> f.getId().equals(o.getParentId())).collect(Collectors.toList());
            if (StringUtils.isEmpty(list2)) {
                //o没有父菜单
                o.setNewParentId(0);
            } else {
                o.setNewParentId(list2.get(0).getNewMenuId());
            }
        }).collect(Collectors.toList());
        //list为数据库应该存储的数据

        List<Menu> list2 = list.stream().filter(o -> !o.getNewMenuId().equals(o.getId())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(list2)) {
            //关联的sys_role_menu表
            List<RoleMenu> roleMenuList = roleMenuMapper.selectList(new LambdaQueryWrapper<>());
            List<RoleMenu> roleMenuUpdateList = new ArrayList<>();
            boolean roleMenuIsNotEmpty = StringUtils.isNotEmpty(roleMenuList);
            list.forEach(menu -> {
                Integer newMenuId = menu.getNewMenuId();
                Integer id = menu.getId();
                menu.setId(newMenuId);
                if (roleMenuIsNotEmpty) {
                    List<RoleMenu> roleMenus = roleMenuList.stream().filter(o -> o.getMenuId().equals(id)).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(roleMenus)) {
                        roleMenus = roleMenuList.stream().peek(o -> o.setMenuId(newMenuId)).collect(Collectors.toList());
                        roleMenuUpdateList.addAll(roleMenus);
                    }
                }
            });
            menuMapper.delete(new LambdaQueryWrapper<>());
            menuMapper.insertListId(list);
            if (roleMenuIsNotEmpty && StringUtils.isNotEmpty(roleMenuUpdateList)) {
                roleMenuMapper.delete(new LambdaQueryWrapper<>());
                roleMenuMapper.insertListId(roleMenuUpdateList);
            }
        }
    }

    /**
     * 更新菜单列表的排序
     */
    @Async
    public void updateMenuOrderNum() {
        updateParentIds.forEach(parentId -> {
            LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Menu::getParentId, parentId);
            List<Menu> menuList = menuMapper.selectList(lqw);
            List<Menu> menus = menuList.stream().sorted(Comparator.comparing(Menu::getOrderNum).thenComparing(Menu::getId)).collect(Collectors.toList());
            List<Menu> menuChange = new ArrayList<>();
            for (int i = 0; i < menus.size(); i++) {
                Menu menu = menus.get(i);
                if (menu.getOrderNum() != (i + 1)) {
                    Menu menu1 = new Menu();
                    menu1.setId(menu.getId());
                    menu1.setOrderNum(i + 1);
                    menuChange.add(menu1);
                }
            }
            if (StringUtils.isNotEmpty(menuChange)) {
                menuMapper.updateMenuList(menuChange);
            }
        });
        updateParentIds = new ArrayList<>();
    }

    /**
     * 更新路由
     */
    public void updateComponent() {
        List<Menu> menus = this.getMenuParentChildren();
        List<Menu> list = new ArrayList<>();
        menus.stream().peek(menu -> {
            boolean T = true;
            if ("首页".equals(menu.getName()) && "/Index".equals(menu.getUrl())) {
                String url = menu.getUrl();
                menu.setComponent(url);
                String name = url.replaceFirst("/", "");
                menu.setComName(name);
                list.add(menu);
                T = false;
            }
            if (T) {
                List<Menu> children = menu.getChildren();
                if (StringUtils.isNotEmpty(children)) {
                    menu.setComponent(null);
                    String component = menu.getUrl();
                    children = children.stream().filter(c -> c.getType().equals(1)).peek(c -> {
                        String url = c.getUrl();
                        c.setComponent(component + url);
                        String name = url.replaceFirst("/", "");
                        c.setComName(name);
                    }).collect(Collectors.toList());
                    list.addAll(children);
                }
            }
        }).collect(Collectors.toList());
        List<Menu> menuList = menuMapper.getMenuList();
        List<Menu> updateList = list.stream().filter(o->!menuList.contains(o)).collect(Collectors.toList());
        if(StringUtils.isNotEmpty(updateList)) {
            menuMapper.updateMenuList(updateList);
        }
    }

    /**
     * 更新按钮的权限
     */
    public void updateButton() {
        List<Menu> menuList = this.getMenuList();
        List<Menu> buttons = menuList.stream().filter(o -> o.getType().equals(2)).collect(Collectors.toList());

        buttons = buttons.stream().peek(o->{
            List<Menu> menus = menuList.stream().filter(menu -> menu.getId().equals(o.getParentId())).collect(Collectors.toList());
            if(StringUtils.isNotEmpty(menus) && menus.size() == 1){
                Menu menu = menus.get(0);
                if(o.getAccess()>menu.getAccess()){
                    o.setAccess(menu.getAccess());
                }
            }
        }).collect(Collectors.toList());
        List<Menu> menus  = this.getMenuList();
        List<Menu> updateList = buttons.stream().filter(o->!menus.contains(o)).collect(Collectors.toList());
        if(StringUtils.isNotEmpty(updateList)) {
            menuMapper.updateMenuList(updateList);
        }
    }

    /**
     * 检查角色菜单表
     */
    @Async
    public void checkRoleMenu() {
        List<RoleMenu> roleMenus = roleMenuMapper.getRoleMenuList();
        if (StringUtils.isNotEmpty(roleMenus)) {
            List<RoleMenu> inserts = roleMenus.stream().filter(o -> "INSERT".equals(o.getRelationship())).collect(Collectors.toList());
            List<RoleMenu> deletes = roleMenus.stream().filter(o -> "DELETE".equals(o.getRelationship())).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(inserts) || StringUtils.isNotEmpty(deletes)) {
                List<Menu> menuList = menuMapper.getMenuList();

                //检查权限本有可以访问菜单，却又添加了数据
                if (StringUtils.isNotEmpty(inserts) && menuList != null) {
                    List<RoleMenu> insertList = inserts.stream().filter(o -> StringUtils.isNotEmpty(menuList.stream().filter(menu -> o.getMenuId().equals(menu.getId()) && menu.getAccess() >= o.getAccess()).collect(Collectors.toList()))).collect(Collectors.toList());

                    //删除数组
                    if (StringUtils.isNotEmpty(insertList)) {
                        roleMenuMapper.deleteList(insertList);
                    }
                }

                //检查权限本就不可以访问菜单，却又添加了数据
                if (StringUtils.isNotEmpty(deletes) && menuList != null) {
                    List<RoleMenu> deleteList = inserts.stream().filter(o -> StringUtils.isNotEmpty(menuList.stream().filter(menu -> o.getMenuId().equals(menu.getId()) && menu.getAccess() < o.getAccess()).collect(Collectors.toList()))).collect(Collectors.toList());

                    //删除数组
                    if (StringUtils.isNotEmpty(deleteList)) {
                        roleMenuMapper.deleteList(deleteList);
                    }
                }
            }

        }
    }
}
