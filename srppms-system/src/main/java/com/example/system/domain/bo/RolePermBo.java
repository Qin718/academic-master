package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.RolePerm;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_role_perm")
@EqualsAndHashCode(callSuper = true)
@Api(value = "角色权限表实体", tags = "角色权限表实体")
public class RolePermBo extends RolePerm{

}