package com.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.entity.UserPerm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface UserPermMapper extends BaseMapper<UserPerm> {

    void insertList(@Param("list") List<UserPerm> list);

}
