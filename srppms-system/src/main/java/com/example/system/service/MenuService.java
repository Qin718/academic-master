package com.example.system.service;

import com.example.common.response.R;
import com.example.system.domain.bo.MenuBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.entity.Menu;
import com.example.system.domain.vo.MenuVo;
import com.example.system.domain.vo.PageVo;

import java.util.List;

public interface MenuService {
    List<MenuVo> getAsideMenus();

    List<Menu> getListMenuParent();

    PageVo getPageVo(PageBo pageBo);

    List<Menu> getMenuRedis();

    R<PageVo> getPageVoSearchByName(MenuBo menuBo, PageBo pageBo);

    void createMenuParentRedis();

    void createMenuRedis();

    String updateMenu(MenuBo menuBo);

    String insertMenu(MenuBo menuBo);

    String deleteMenu(List<Integer> list);

    List<Integer> getListByRole(Integer roleId);

    R<String> changeRoleMenu(List<Integer> newMenu, List<Integer> oldMenu, Integer roleId);

    List<MenuVo> getButton();
}
