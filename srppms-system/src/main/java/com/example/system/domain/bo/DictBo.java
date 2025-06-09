package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Dict;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_dict")
@EqualsAndHashCode(callSuper = true)
@Api(value = "字典表实体", tags = "字典表实体")
public class DictBo extends Dict{

}