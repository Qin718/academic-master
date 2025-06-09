package com.example.system.domain.model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api(value = "项目种类实体", tags = {"项目种类实体"})
public class ProjectKind {

    @ApiModelProperty("数量")
    private Integer value;

    @ApiModelProperty("种类名字")
    private String name;

    @ApiModelProperty("颜色")
    private String itemStyle;

}
