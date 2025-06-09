package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.common.prop.Prop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_config")
@Api(value = "参数表实体", tags = {"参数表实体"})
public class Config {
    @TableId
    @Prop(label = "编号")
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("标识符")
    private String config;

    @ApiModelProperty("参数内容")
    private String value;

    @ApiModelProperty("备注")
    private String remark;
}
