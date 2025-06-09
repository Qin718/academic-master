package com.example.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.system.domain.entity.Friend;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@TableName("sys_friend")
@EqualsAndHashCode(callSuper = true)
@Api(value = "好友关系表实体", tags = "好友关系表实体")
public class FriendVo extends Friend{
    @ApiModelProperty("图片压缩字节流")
    private Map<String, byte[]> imageByte;
}