package com.example.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目日志(SysItem)表数据库访问层
 */
@Mapper
public interface ItemMapper extends BaseMapper<Item> {

    List<Item> getItemList();

    void updateItem(@Param("item") Item item);

    void insertItem(Item item);
}

