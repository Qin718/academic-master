package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Perm;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_perm")
@EqualsAndHashCode(callSuper = true)
@Api(value = "权限表实体", tags = "权限表实体")
public class PermBo extends Perm{

}