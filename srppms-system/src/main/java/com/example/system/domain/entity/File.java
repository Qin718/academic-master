package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_file")
@Api(value = "附件表实体", tags = {"附件表实体"})
public class File {
    @TableId
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("数据流")
    private String stream;

    @ApiModelProperty("项目id")
    private Integer projectId;
}

