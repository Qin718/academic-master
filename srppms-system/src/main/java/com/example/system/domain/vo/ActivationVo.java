package com.example.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Activation;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_activation")
@EqualsAndHashCode(callSuper = true)
@Api(value = "活跃度表实体", tags = "活跃度表实体")
public class ActivationVo extends Activation{

}