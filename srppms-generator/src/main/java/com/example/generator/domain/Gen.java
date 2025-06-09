package com.example.generator.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@TableName("sys_gen")
@Api(value = "代码实体", tags = {"代码实体"})
public class Gen {
    @TableId
    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("表名注释")
    private String tableComment;

    @ApiModelProperty("表数据引擎")
    private String engine;

    @ApiModelProperty("表字符集")
    private String tableCollation;

    @ApiModelProperty("表中数据条数")
    private String tableRows;

    @ApiModelProperty("表创建时间")
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
    @DateTimeFormat(pattern = "YYYY-MM-DD HH:mm:ss")
    private String createTime;

}
