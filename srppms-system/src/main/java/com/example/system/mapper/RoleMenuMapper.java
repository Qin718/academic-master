package com.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.entity.RoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    void insertList(@Param("list") List<RoleMenu> list);

    void deleteList(@Param("list") List<RoleMenu> list);

    List<RoleMenu> getRoleMenuList();

    void insertListId(List<RoleMenu> list);

}
