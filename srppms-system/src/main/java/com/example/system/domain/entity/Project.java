package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.common.prop.Prop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_project")
@Api(value = "项目内容表实体", tags = {"项目内容表实体"})
public class Project {
    @TableId
    @Prop(isProp = false)
    @ApiModelProperty("主键")
    private Integer id;

    @Prop(index = 2, prop = "project.name")
    @ApiModelProperty("项目名称")
    private String name;

    @Prop(isProp = false)
    @ApiModelProperty("项目内容")
    private String content;

    @Prop(index = 3, prop = "project.type")
    @ApiModelProperty("科研类型")
    private String type;

    @Prop(isProp = false)
    @ApiModelProperty("申请资金")
    private Double money;

    @Prop(isProp = false)
    @ApiModelProperty("项目说明")
    private String remark;

    @Prop(isProp = false)
    @TableField(exist = false)
    @ApiModelProperty("附件列表-保存格式为文件的路径")
    private String files;
}

