package com.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.entity.RolePerm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePermMapper extends BaseMapper<RolePerm> {

    void insertList(@Param("list") List<RolePerm> list);

    void deleteList(@Param("list") List<RolePerm> list);
}
