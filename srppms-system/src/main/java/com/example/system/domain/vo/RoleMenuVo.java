package com.example.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.RoleMenu;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_role_menu")
@EqualsAndHashCode(callSuper = true)
@Api(value = "角色菜单表实体", tags = "角色菜单表实体")
public class RoleMenuVo extends RoleMenu{

}