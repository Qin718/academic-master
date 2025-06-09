package com.example.system.domain.model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@Api(value = "类实体", tags = {"类实体"})
public class Domain {
    @ApiModelProperty("类名称")
    private String name;

    @ApiModelProperty("swagger名称")
    private String apiValue;

    @ApiModelProperty("数据库表")
    private String tableValue;

    @ApiModelProperty("字段")
    private List<Field> field;
}
