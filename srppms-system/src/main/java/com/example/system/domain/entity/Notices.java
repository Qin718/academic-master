package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.common.prop.Prop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_notices")
@Api(value = "系统公告表实体", tags = {"系统公告表实体"})
public class Notices {
    @TableId
    @Prop(label = "公告编号")
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("公告名称")
    private String title;

    @ApiModelProperty("公告内容")
    private String content;

    @ApiModelProperty("公告编号")
    private String noticeId;

    @ApiModelProperty("发布时间")
    private String time;

    @ApiModelProperty("图标")
    private String icon;

}

