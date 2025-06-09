package com.example.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.generator.domain.Gen;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GenMapper extends BaseMapper<Gen> {
    /**
     * 获取所有表信息数据
     *
     * @return List<Gen>
     */
    List<Gen> getColumnDetailMapVo(@Param("database") String database);

    /**
     * 获取所有表中字段信息数据
     *
     * @param tableName 表名
     * @return List<Gen>
     */
    List<Gen> getColumnDetailMapVoByTableName(@Param("database") String database, @Param("tableName") String tableName);

}
