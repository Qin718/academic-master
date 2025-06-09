package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_dict")
@Api(value = "字典表实体", tags = {"字典表实体"})
public class Dict {
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("字典类型")
    private String type;

    @ApiModelProperty("字典名字")
    private String name;

    @ApiModelProperty("字典内容")
    private String dict;

}

