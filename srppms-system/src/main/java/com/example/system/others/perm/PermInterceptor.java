package com.example.system.others.perm;

import cn.dev33.satoken.stp.StpUtil;
import com.example.common.constant.RedisConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.utils.StringUtils;
import com.example.system.service.impl.PermServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@Api(value = "权限拦截器", tags = {"权限拦截器"})
public class PermInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private PermServiceImpl permServiceImpl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        PermInter permInter = null;
        try {
            permInter = ((HandlerMethod) handler).getMethodAnnotation(PermInter.class);
        }catch (Exception e){
            System.out.println("发生错误:" + e.getMessage());
        }
        //如果没有添加权限注解则直接跳过允许访问
        if (permInter == null) {
            return true;
        }
        //获取注解中的值
        String perm = permInter.perm();
        String account = (String) StpUtil.getLoginId();
        String key = RedisConstant.GET_LIST_PERM + account;
        if (redisUtils.noHasKey(key)) {
            permServiceImpl.createUserPermList(account);
        }
        List<String> list = redisUtils.get(key, String.class);
        if (StringUtils.isNotEmpty(list) && list.contains(perm)) {
            return true;
        }
        throw new Exception("您没有 '" + permInter.name() + "' 权限！");
    }

}
