package com.example.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Friend;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_friend")
@EqualsAndHashCode(callSuper = true)
@Api(value = "好友关系表实体", tags = "好友关系表实体")
public class FriendBo extends Friend{

}