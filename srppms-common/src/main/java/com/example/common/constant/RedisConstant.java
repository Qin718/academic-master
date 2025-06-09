package com.example.common.constant;

import io.swagger.annotations.Api;

@Api(value = "缓存常量", tags = {"缓存常量"})
public class RedisConstant {
    /**
     * 验证码缓存
     */
    public static final String CODE = "code" ;
    /**
     * 验证码缓存
     * 时间5分钟
     */
    public static final long CODE_TIME = 60 * 5;
    /**
     * 账号权限列表
     */
    public static final String GET_LIST_PERM = "getList:Perm:";
    /**
     * 账号权限列表缓存-保存时间-永久
     */
    public static final long GET_LIST_PERM_TIME = -1;
    /**
     * 缓存-Menu列表
     */
    public static final String GET_LIST_MENU = "getList:Menu";
    /**
     * 缓存-Menu列表缓存-保存时间-永久
     */
    public static final long GET_LIST_MENU_TIME = -1;
    /**
     * 缓存-Menu列表
     */
    public static final String GET_LIST_MENU_ASIDE = "getList:Menu:Aside";
    /**
     * 缓存-Menu列表缓存-保存时间-永久
     */
    public static final long GET_LIST_MENU_ASIDE_TIME = 60;
    /**
     * 缓存-Role列表
     */
    public static final String GET_LIST_ROLE = "getList:Role";
    /**
     * 缓存-Role列表缓存-保存时间-永久
     */
    public static final long GET_LIST_ROLE_TIME = -1;
    /**
     * 缓存-Menu列表
     */
    public static final String GET_LIST_MENU_PARENT = "getList:Menu:Parent";
    /**
     * 缓存-Menu列表缓存-保存时间-永久
     */
    public static final long GET_LIST_MENU_PARENT_TIME = -1;
    /**
     * 缓存-Notice列表
     */
    public static final String GET_LIST_NOTICE = "getList:Notice" ;
    /**
     * 缓存-Notice列表缓存-保存时间-永久
     */
    public static final long GET_LIST_NOTICE_TIME = -1;
    /**
     * 缓存-Item列表
     */
    public static final String GET_LIST_ITEM = "getList:Item" ;
    /**
     * 缓存-Item列表缓存-保存时间-永久
     */
    public static final long GET_LIST_ITEM_TIME = -1;
    /**
     * 缓存-ItemMyList列表
     */
    public static final String GET_LIST_ITEM_MY_LIST = GET_LIST_ITEM + ":" ;
    /**
     * 缓存-ItemMyList列表缓存-保存时间-永久
     */
    public static final long GET_LIST_ITEM_MY_LIST_TIME = -1;
    /**
     * 缓存-审核人员列表
     */
    public static final String GET_LIST_SHARE_USER = "getList:ShareUser" ;
    /**
     * 缓存-审核人员列表缓存-保存时间-永久
     */
    public static final long GET_LIST_SHARE_USER_TIME = -1;
    /**
     * 缓存-用户上传的项目附件
     */
    public static final String PROJECT_FILES = "getList:Project:File:";
    /**
     * 缓存-用户上传的项目附件-保存时间-永久
     */
    public static final long PROJECT_FILES_T_IME = -1;
    /**
     * 字典缓存
     */
    public static final String DICT = "Dict:" ;
    /**
     * 项目版本
     */
    public static final String VERSION = "Version";
}
