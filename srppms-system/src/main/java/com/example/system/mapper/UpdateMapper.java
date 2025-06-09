package com.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.entity.Update;
import com.example.system.domain.vo.UpdateVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UpdateMapper extends BaseMapper<Update> {

    List<UpdateVo> getList();

    void insertUpdateId(@Param("update") Update update);
}
