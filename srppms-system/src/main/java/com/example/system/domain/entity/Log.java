package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.common.prop.Prop;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_log")
@Api(value = "系统日志表实体", tags = {"系统日志表实体"})
public class Log {
    @TableId
    @Prop(label = "编号")
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("请求方式")
    private String method;

    @ApiModelProperty("请求地址")
    private String url;

    @ApiModelProperty("请求接口")
    private String uri;

    @ApiModelProperty("请求参数")
    private String params;

    @ApiModelProperty("执行时长")
    private Double time;

    @ApiModelProperty("ip地址")
    private String ip;

    @ApiModelProperty("访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @Prop(isProp = false)
    @ApiModelProperty("请求映射控制类")
    private String controlClass;

    @ApiModelProperty("请求来源")
    private String innerIp;

    @ApiModelProperty("设备来源")
    private String mobile;

    @Prop(isProp = false)
    @ApiModelProperty("返回提示类型")
    private String type;

    @Prop(isProp = false)
    @ApiModelProperty("返回状态")
    private Integer code;

    @Prop(isProp = false)
    @ApiModelProperty("返回提示")
    private String message;

    @Prop(isProp = false)
    @ApiModelProperty("返回结果")
    private String result;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty("浏览器类型")
    private String browser;

    @ApiModelProperty("方法名称")
    private String methodApi;

    @ApiModelProperty("类名称")
    private String classApi;
}
