package com.example.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.entity.Activation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivationMapper extends BaseMapper<Activation> {
    void insertActivation(Activation activation);
}
