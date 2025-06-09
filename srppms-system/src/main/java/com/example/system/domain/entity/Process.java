package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_process")
@Api(value = "项目审核表实体", tags = {"项目审核表实体"})
public class Process {
    @TableId
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("项目id")
    private Integer projectId;

    @ApiModelProperty("审核人员id")
    private Integer userId;

    @ApiModelProperty("审核结果")
    private String process = "未审核";

    @ApiModelProperty("审核意见")
    private String remark = "未审核";

    @ApiModelProperty("是否审核过")
    private String isProcess = "NO";

}

