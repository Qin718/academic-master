package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_role_menu")
@Api(value = "角色菜单表实体", tags = {"角色菜单表实体"})
public class RoleMenu {
    @TableId
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("菜单ID")
    private Integer menuId;

    @ApiModelProperty("角色ID")
    private Integer roleId;

    @ApiModelProperty("角色级别")
    private Integer access;

    @ApiModelProperty("对应关系")
    private String relationship;

    @TableField(exist = false)
    @ApiModelProperty("新菜单ID")
    private Integer newMenuId;

}
