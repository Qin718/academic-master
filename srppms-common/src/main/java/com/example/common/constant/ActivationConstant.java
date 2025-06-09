package com.example.common.constant;

import io.swagger.annotations.Api;

@Api(value = "活跃度常量", tags = {"活跃度常量"})
public class ActivationConstant {
    /**
     * 活跃度系数-每日登录
     */
    public static final double ACTIVATION_LOGIN = 1.0;
    /**
     * 活跃度常量-每日登录
     */
    public static final String LOGIN = "Login";
    /**
     * 活跃度系数-创建项目
     */
    public static final double ACTIVATION_ADD_ITEM = 5.0;
    /**
     * 活跃度常量-创建项目
     */
    public static final String ADD_ITEM = "AddItem";
    /**
     * 活跃度系数-审核项目
     */
    public static final double ACTIVATION_PROCESS = 3.0;
    /**
     * 活跃度常量-审核项目
     */
    public static final String PROCESS = "Process";
    /**
     * 活跃度系数-打分
     */
    public static final double ACTIVATION_SCORE = 10.0;
    /**
     * 活跃度常量-打分
     */
    public static final String SCORE = "Score";
}
