package com.example.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Image;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_image")
@EqualsAndHashCode(callSuper = true)
@Api(value = "图片表实体", tags = "图片表实体")
public class ImageVo extends Image{

}