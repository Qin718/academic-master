package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Log;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_log")
@EqualsAndHashCode(callSuper = true)
@Api(value = "系统日志表实体", tags = "系统日志表实体")
public class LogBo extends Log{

}