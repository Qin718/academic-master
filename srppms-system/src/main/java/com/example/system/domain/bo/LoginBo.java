package com.example.system.domain.bo;

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
public class LoginBo extends Login{
    @ApiModelProperty(value = "账号", required = true)
    private String account;

    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty(value = "验证码", required = true)
    private String code;

    @ApiModelProperty(value = "记住密码")
    private Boolean remember;

    @ApiModelProperty(value = "自动登录")
    private Boolean auto;

}