package com.example.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.UserPerm;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_user_perm")
@EqualsAndHashCode(callSuper = true)
@Api(value = "账号权限表实体", tags = "账号权限表实体")
public class UserPermVo extends UserPerm{

}