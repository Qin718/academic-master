package com.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.bo.RoleBo;
import com.example.system.domain.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    void insertBo(@Param("bo") RoleBo bo);

    Role getRoleByAccount(@Param("account") String account);

    Role getRoleByUserId(@Param("userId") Integer userId);

    List<Role> getList();
}
