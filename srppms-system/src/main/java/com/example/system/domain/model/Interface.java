package com.example.system.domain.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.example.common.prop.Prop;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Api(value = "接口实体", tags = {"接口实体"})
public class Interface {
    @TableId
    @Prop(isProp = false)
    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty("方法名称")
    private String method;

    @ApiModelProperty("类名称")
    private String clazz;

    @ApiModelProperty("最后一次访问账号")
    private String account;

    @ApiModelProperty("最后一次访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @ApiModelProperty("平均耗时")
    private Float average;
}
