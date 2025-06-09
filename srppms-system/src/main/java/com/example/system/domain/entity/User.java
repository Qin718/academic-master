package com.example.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.common.expect.expectToExcel;
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
@TableName("sys_user")
@Api(value = "系统用户表实体", tags = {"系统用户表实体"})
public class User {
    @TableId
    @Prop(label = "编号")
    @ApiModelProperty("主键")
    @expectToExcel(header = "编号",template = false)
    private Integer id;

    @ApiModelProperty("账号")
    @expectToExcel(header = "账号",template = false)
    private String account;

    @ApiModelProperty("用户名")
    @expectToExcel(header = "用户名")
    private String username;

    @ApiModelProperty("用户姓名")
    @expectToExcel(header = "用户姓名")
    private String name;

    @Prop(isProp = false)
    @ApiModelProperty("密码")
    @expectToExcel(header = "密码")
    private String password;

    @Prop(isProp = false)
    @ApiModelProperty("盐")
    @expectToExcel(header = "盐",template = false)
    private String salt;

    @ApiModelProperty("账号状态")
    @expectToExcel(header = "账号状态",template = false)
    private String status;

    @ApiModelProperty("邮箱")
    @expectToExcel(header = "邮箱")
    private String email;

    @ApiModelProperty("手机号码")
    @expectToExcel(header = "手机号码")
    private String mobile;

    @ApiModelProperty("性别")
    @expectToExcel(header = "性别")
    private String sex;

    @ApiModelProperty("创建时间")
    @expectToExcel(header = "创建时间",template = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @ApiModelProperty("备注")
    @expectToExcel(header = "备注")
    private String remark;

    @Prop(isProp = false)
    @ApiModelProperty("头像")
    @TableField(exist = false)
    private String image;

    @Prop(isProp = false)
    @ApiModelProperty("头像Id")
    private Integer imageId;

    @Prop(isProp = false)
    @TableField(exist = false)
    @ApiModelProperty("角色Id")
    private Integer roleId;
}
