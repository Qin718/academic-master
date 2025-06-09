package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Role;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_role")
@EqualsAndHashCode(callSuper = true)
@Api(value = "系统角色表实体", tags = "系统角色表实体")
public class RoleBo extends Role{

}