package com.example.system.domain.model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api(value = "WebSocket通知实体", tags = {"WebSocket通知实体"})
//https://element.eleme.io/#/zh-CN/component/notification
public class Notify {
    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String message;

    @ApiModelProperty("存在时间")//0未不关闭，单位毫秒
    private Integer duration;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("图标")
    private String icon;

    @Override
    public String toString() {
        return "{" +
                "\"title\"=\"" + title + "\"" +
                ",\"message\"=\"" + message + "\"" +
                ",\"duration\"=\"" + duration + "\"" +
                ",\"color\"=\"" + color + "\"" +
                ",\"icon\"=\"" + icon + "\"" +
                "}";
    }
}

