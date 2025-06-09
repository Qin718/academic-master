package com.example.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.User;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = true)
@Api(value = "系统用户表实体", tags = "系统用户表实体")
public class UserVo extends User{
    @TableField(exist = false)
    private String roleName;

    @TableField(exist = false)
    private Integer roleId;

    @TableField(exist = false)
    private Integer access;
}