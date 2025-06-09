package com.example.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.File;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_file")
@EqualsAndHashCode(callSuper = true)
@Api(value = "附件表实体", tags = "附件表实体")
public class FileVo extends File{

}