package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Project;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_project")
@EqualsAndHashCode(callSuper = true)
@Api(value = "项目内容表实体", tags = "项目内容表实体")
public class ProjectBo extends Project{
    @ApiModelProperty("项目评分")
    private Double score;

    @ApiModelProperty("创建人")
    private String createByName;

    @ApiModelProperty("项目状态")
    private String state;
}