package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_friend")
@Api(value = "好友关系表实体", tags = {"好友关系表实体"})
public class Friend {
    @TableId
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("用户A的id")
    private Integer userIdA;

    @ApiModelProperty("用户B的id")
    private Integer userIdB;

    @ApiModelProperty("用户A的账号")
    private String userA;

    @ApiModelProperty("用户B的账号")
    private String userB;

    @ApiModelProperty("A向B发起好友申请，流程状态（1.默认，2.同意，3.拒绝）")
    private Integer status;

    @ApiModelProperty("A对B的备注")
    private String notes;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private String image;

    @TableField(exist = false)
    private String name;
}

