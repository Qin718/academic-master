package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_image")
@Api(value = "图片表实体", tags = {"图片表实体"})
public class Image {
    @TableId
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("图片数据流")
    private String image;
}
