package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = true)
@Api(value = "系统用户表实体", tags = "系统用户表实体")
public class UserBo extends User{
    @ApiModelProperty("旧密码")
    private String oldPassword;
}