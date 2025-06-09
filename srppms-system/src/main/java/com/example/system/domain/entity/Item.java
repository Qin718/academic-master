package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.common.prop.Prop;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_item")
@Api(value = "项目日志表实体", tags = {"项目日志表实体"})
public class Item {
    @TableId
    @Prop(label = "编号", index = 1)
    @ApiModelProperty("主键")
    private Integer id;

    @Prop(index = 5)
    @ApiModelProperty("创建者id")
    private Integer createBy;

    @Prop(index = 6)
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @Prop(index = 7)
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @Prop(isProp = false)
    @ApiModelProperty("项目id")
    private Integer projectId;

    @Prop(index = 4)
    @TableField(exist = false)
    @ApiModelProperty("项目状态")
    private String status;

    @Prop(isProp = false)
    @TableField(exist = false)
    @ApiModelProperty("项目内容实体")
    private Project project;

    @Prop(isProp = false)
    @TableField(exist = false)
    @ApiModelProperty("用户/创建人实体")
    private User user;

    @Prop(isProp = false)
    @TableField(exist = false)
    @ApiModelProperty("项目评分")
    private Double score;

    @Prop(isProp = false)
    @TableField(exist = false)
    @ApiModelProperty("项目审核")
    private Process process;

}

