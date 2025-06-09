package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Item;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_item")
@EqualsAndHashCode(callSuper = true)
@Api(value = "项目日志表实体", tags = "项目日志表实体")
public class ItemBo extends Item{

}