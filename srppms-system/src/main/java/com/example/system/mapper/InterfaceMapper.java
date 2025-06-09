package com.example.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.model.Interface;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InterfaceMapper extends BaseMapper<Interface> {
    List<Interface> getInterfaceList();
}
