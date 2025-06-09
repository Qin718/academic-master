package com.example.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class ProjectVo extends Project{
    @TableField(exist = false)
    @ApiModelProperty("项目审核状态")
    private String status;

    @TableField(exist = false)
    private String username;
}