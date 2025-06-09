package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Notices;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_notices")
@EqualsAndHashCode(callSuper = true)
@Api(value = "系统公告表实体", tags = "系统公告表实体")
public class NoticesBo extends Notices{

}