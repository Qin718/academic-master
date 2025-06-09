package com.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.bo.MenuBo;
import com.example.system.domain.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getMenuList();

    void insertMenu(@Param("menu") MenuBo menu);

    void updateMenuList(@Param("list") List<Menu> menus);

    void insertListId(@Param("list") List<Menu> list);
}
