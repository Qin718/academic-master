package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_user_role")
@Api(value = "用户角色表实体", tags = {"用户角色表实体"})
public class UserRole {
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("user_id")
    private Integer userId;

    @ApiModelProperty("role_id")
    private Integer roleId;

}
