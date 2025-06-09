package com.example.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.entity.Process;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysProcess)表数据库访问层
 */
@Mapper
public interface ProcessMapper extends BaseMapper<Process> {
    void insertProcessList(@Param("List") List<Process> insertList);

    void updateList(@Param("List") List<Process> processList);

    List<Integer> checkUserProcessMyItem();
}

