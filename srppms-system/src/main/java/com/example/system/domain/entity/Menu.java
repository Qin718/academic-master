package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.common.prop.Prop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "sys_menu", autoResultMap = true)
@Api(value = "系统菜单表实体", tags = {"系统菜单表实体"})
public class Menu {
    @TableId
    @Prop(label = "编号")
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("父菜单id")
    private Integer parentId;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("菜单URL")
    private String url;

    @Prop(label = "类型")
    @ApiModelProperty("类型   0：目录   1：菜单   2：按钮")
    private Integer type;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("排序")
    private Integer orderNum;

    @ApiModelProperty("权限")
    private Integer access;

    @Prop(isProp = false)
    @TableField(exist = false)
    private List<Menu> children;

    @Prop(isProp = false)
    @TableField(exist = false)
    private String parentName;

    @Prop(isProp = false)
    @ApiModelProperty("新Id")
    @TableField(exist = false)
    private Integer newMenuId;

    @Prop(isProp = false)
    @TableField(exist = false)
    @ApiModelProperty("新父菜单ID")
    private Integer newParentId;

    @ApiModelProperty("路由地址")
    private String component;

    @ApiModelProperty("路由名称")
    private String comName;

    @ApiModelProperty("是否显示")
    private String isShow;
}
