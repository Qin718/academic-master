package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Update;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_update")
@EqualsAndHashCode(callSuper = true)
@Api(value = "更新日志表实体", tags = "更新日志表实体")
public class UpdateBo extends Update {

}