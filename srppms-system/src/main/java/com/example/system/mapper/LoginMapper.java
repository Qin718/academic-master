package com.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.entity.Login;
import com.example.system.domain.vo.LoginVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoginMapper extends BaseMapper<Login> {
    void insertLogin(@Param("login") Login login);

    List<LoginVo> getList();
}
