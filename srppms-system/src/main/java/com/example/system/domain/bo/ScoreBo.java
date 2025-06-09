package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Score;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_score")
@EqualsAndHashCode(callSuper = true)
@Api(value = "评分表实体", tags = "评分表实体")
public class ScoreBo extends Score{

}