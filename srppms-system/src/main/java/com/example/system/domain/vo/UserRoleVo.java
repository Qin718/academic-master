package com.example.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.UserRole;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_user_role")
@EqualsAndHashCode(callSuper = true)
@Api(value = "用户角色表实体", tags = "用户角色表实体")
public class UserRoleVo extends UserRole{

}