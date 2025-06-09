package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_score")
@Api(value = "评分表实体", tags = {"评分表实体"})
public class Score {
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("项目ID")
    private Integer projectId;

    @ApiModelProperty("得分")
    private Double score;
}
