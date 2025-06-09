package com.example.web.others.runner;

import com.example.common.constant.SystemConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.utils.DirUtils;
import com.example.system.service.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RunnerInit {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MenuService menuService;
    @Autowired
    private UpdateService updateService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private PermService permService;

    @Async
    @SneakyThrows
    @PostConstruct
    public void init() {
                          //===============删除本地缓存===============
        redisUtils.delAll();
                          //===============删除本地文件===============
        DirUtils.deleteDir(SystemConstant.SRPPMS);
        System.out.println("===============初始化权限===============");
        long time1 = System.currentTimeMillis();
        permService.createPermRedis();
        System.out.println("===============检查数据库表===============");
        long time2 = System.currentTimeMillis();
        commonService.checkDataBase();
        System.out.println("===============初始化父菜单===============");
        long time3 = System.currentTimeMillis();
        menuService.createMenuParentRedis();
        System.out.println("===============初始化菜单=================");
        long time4 = System.currentTimeMillis();
        menuService.createMenuRedis();
        System.out.println("===============初始化项目=================");
        long time5 = System.currentTimeMillis();
        itemService.createItemRedis();
        System.out.println("===============初始化角色=================");
        long time6 = System.currentTimeMillis();
        roleService.createRoleRedis();
        System.out.println("===============初始化日志=================");
        long time7 = System.currentTimeMillis();
        updateService.createUpdateRedis();
        System.out.println("===============初始化审核=================");
        long time8 = System.currentTimeMillis();
        processService.createShareUserRedis();
        long time9 = System.currentTimeMillis();
        System.out.println("==============各项耗时如下=================");
        System.out.printf("\t\t\t初始化权限:%d ms\n", time2 - time1);
        System.out.printf("\t\t\t检查数据库:%d ms\n", time3 - time2);
        System.out.printf("\t\t\t初始父菜单:%d ms\n", time4 - time3);
        System.out.printf("\t\t\t初始化菜单:%d ms\n", time5 - time4);
        System.out.printf("\t\t\t初始化项目:%d ms\n", time6 - time5);
        System.out.printf("\t\t\t初始化角色:%d ms\n", time7 - time6);
        System.out.printf("\t\t\t初始化日志:%d ms\n", time8 - time7);
        System.out.printf("\t\t\t初始化审核:%d ms\n", time9 - time8);
        System.out.println("===============初始化完成=================");
    }

}
