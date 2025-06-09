package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Process;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_process")
@EqualsAndHashCode(callSuper = true)
@Api(value = "项目审核表实体", tags = "项目审核表实体")
public class ProcessBo extends Process{

}