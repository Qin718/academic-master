package com.example.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Login;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_login")
@EqualsAndHashCode(callSuper = true)
@Api(value = "登录日志表实体", tags = "登录日志表实体")
public class LoginVo extends Login{
    @ApiModelProperty(value = "验证码", required = true)
    private String code;
}