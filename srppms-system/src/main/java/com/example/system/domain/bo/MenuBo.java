package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Menu;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_menu")
@EqualsAndHashCode(callSuper = true)
@Api(value = "系统菜单表实体", tags = "系统菜单表实体")
public class MenuBo extends Menu{

}