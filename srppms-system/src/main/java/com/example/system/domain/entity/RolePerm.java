package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_role_perm")
@Api(value = "角色权限表实体", tags = {"角色权限表实体"})
public class RolePerm {
    @TableId
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("权限ID")
    private Integer permId;

    @ApiModelProperty("角色ID")
    private Integer roleId;

    @ApiModelProperty("对应关系")
    private String relationship;

}
