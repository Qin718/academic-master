package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@TableName("sys_update")
@Api(value = "更新日志表实体", tags = {"更新日志表实体"})
public class Update {

    @TableId("id")
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("发布时间")
    private String time;

    @ApiModelProperty("版本")
    private String version;

    @ApiModelProperty("提交者")
    private String name;

    @TableField(exist = false)
    @ApiModelProperty("内容列表")
    private List<String> contents;
}