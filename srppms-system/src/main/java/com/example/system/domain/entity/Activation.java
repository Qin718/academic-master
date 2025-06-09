package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_activation")
@Api(value = "活跃度表实体", tags = {"活跃度表实体"})
public class Activation {
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("用户Id")
    private Integer userId;

    @ApiModelProperty("总活跃度")
    private Double allActivation = 0.0;

    @ApiModelProperty("浏览活跃度")
    private Double pageView = 0.0;

    @ApiModelProperty("创建项目活跃度")
    private Double addItem = 0.0;

    @ApiModelProperty("评分活跃度")
    private Double scoreProject = 0.0;

    @ApiModelProperty("登录活跃度")
    private Double login = 0.0;

    @ApiModelProperty("审核项目活跃度")
    private Double process = 0.0;
}
