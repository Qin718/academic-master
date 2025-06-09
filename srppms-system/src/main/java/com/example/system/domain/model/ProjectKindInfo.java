package com.example.system.domain.model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Api(value = "数据统计实体", tags = {"数据统计实体"})
public class ProjectKindInfo {
    @ApiModelProperty("新增项目")
    private Integer newAdd = 0;

    @ApiModelProperty("成功项目")
    private Integer success = 0;

    @ApiModelProperty("申请资金")
    private Double money = 0.0;

    @ApiModelProperty("去年最成功创作者")
    private List<String> name = new ArrayList<>();

    @ApiModelProperty("去年最受欢迎的项目")
    private String itemName = "";

    @ApiModelProperty("获赞数量")
    private long scoreNumber = 0;

}
