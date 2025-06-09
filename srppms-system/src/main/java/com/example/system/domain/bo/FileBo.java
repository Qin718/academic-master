package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.File;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_file")
@EqualsAndHashCode(callSuper = true)
@Api(value = "附件表实体", tags = "附件表实体")
public class FileBo extends File{

}