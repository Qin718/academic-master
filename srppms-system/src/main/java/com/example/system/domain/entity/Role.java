package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.common.prop.Prop;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_role")
@Api(value = "系统角色表实体", tags = {"系统角色表实体"})
public class Role {
    @TableId
    @Prop(label = "编号")
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色权限")
    private Integer access;

    @Prop(index = 8)
    @ApiModelProperty("备注")
    private String remark;

    @Prop(index = 4)
    @ApiModelProperty("创建者Iid")
    private Integer createBy;

    @Prop(index = 5)
    @TableField(exist = false)
    @ApiModelProperty("创建者名称")
    private String createByName;

    @Prop(index = 6)
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @Prop(label = "活跃度", index = 7)
    @ApiModelProperty("权限需要的活跃度")
    private Double activation;

    @Prop(isProp = false)
    @ApiModelProperty("是否可以通过活跃度获取到此权限")
    private String byAct;
}
