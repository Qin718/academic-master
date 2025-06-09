package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_user_perm")
@Api(value = "账号权限表实体", tags = {"账号权限表实体"})
public class UserPerm {
    @TableId
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("账号ID")
    private Integer userId;

    @ApiModelProperty("权限ID")
    private Integer permId;
}
