package com.example.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Config;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_config")
@EqualsAndHashCode(callSuper = true)
@Api(value = "参数表实体", tags = "参数表实体")
public class ConfigVo extends Config{

}