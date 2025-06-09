package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_perm")
@Api(value = "权限表实体", tags = {"权限表实体"})
public class Perm {
    @TableId
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("权限")
    private String perm;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("需要的角色级别")
    private Integer jsjb;
}
