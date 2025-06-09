package com.example.web.others.aop;

import cn.dev33.satoken.stp.StpUtil;
import com.example.common.constant.RedisConstant;
import com.example.common.constant.SystemConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.response.R;
import com.example.common.utils.*;
import com.example.system.domain.entity.Log;
import com.example.system.others.perm.PermInter;
import com.example.system.service.LogService;
import com.example.system.service.impl.PermServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class RequestLogAspect {
    public final RequestLogAspect requestLogAspect = this;
    private final Logger logger = LoggerFactory.getLogger(RequestLogAspect.class);
    @Autowired
    private LogService logService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private PermServiceImpl permServiceImpl;

    @Pointcut("execution(* com.example.web.controller..*(..))")
    public void requestServer() {
    }

    @SneakyThrows
    @Around("requestServer()")
    public Object doAround(ProceedingJoinPoint pjp) {
        //记录请求开始执行时间：
        long beginTime = System.currentTimeMillis();
        //获取请求信息
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request;
        if (sra != null) {
            request = sra.getRequest();
        }else {
            return null;
        }

//        //检查账号权限
//        this.preHandle(pjp);

        String ip = IPUtils.getIp(request);
        String innerIp = IPUtils.innerIp(ip);
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String methodName = pjp.getSignature().getName();
        String clazzName = pjp.getTarget().getClass().getSimpleName();
        String mobile = MobileUtils.getDeviceFrom(DeviceUtils.getCurrentDevice(request));

        //获取请求参数：
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        //获取请求参数类型
        String[] parameterNames = ms.getParameterNames();
        //获取请求参数值
        Object[] parameterValues = pjp.getArgs();
        StringBuilder params = new StringBuilder();
        if (!Arrays.asList(SystemConstant.NOT_OUTPUT_AND_RECORD).contains(uri)) {
            //组合请求参数，进行日志打印
            if (parameterNames != null && parameterNames.length > 0) {
                for (int i = 0; i < parameterNames.length; i++) {
                    if ("bindingResult".equals(parameterNames[i])) {
                        break;
                    }
                    if ((parameterValues[i] instanceof HttpServletRequest) || (parameterValues[i] instanceof HttpServletResponse)) {
                        params.
                                append("[").
                                append(parameterNames[i]).append("=").append(parameterValues[i])
                                .append("]");
                    } else {
                        params.
                                append("[").
                                append(parameterNames[i]).append("=")
                                .append(StringUtils.tojsonstring(parameterValues[i]))
                                .append("]");
                    }
                }
            }
        }
        Object result;
        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            //请求操纵失败
            //记录错误日志
            String loggerContent = "ε=ε=ε=ε=ε=ε=┌(;￣◇￣)┘          切面处理请求错误！ ";
            if (StringUtils.isNotEmpty(ip)) {
                loggerContent += "\tIP信息(ง•̀_•́)ง->： 【" + ip + "】";
            }
            if (StringUtils.isNotEmpty(uri)) {
                loggerContent += "\tURI信息(ง•̀_•́)ง->： 【" + uri + "】";
            }
            if (StringUtils.isNotEmpty(clazzName)) {
                loggerContent += "\t请求映射控制类(ง•̀_•́)ง->：【" + clazzName + "】";
            }
            if (StringUtils.isNotEmpty(methodName)) {
                loggerContent += "\t请求方法(ง•̀_•́)ง->：【" + methodName + "】";
            }
            if (StringUtils.isNotEmpty(mobile)) {
                loggerContent += "\t请求设备类型(ง•̀_•́)ง->：【" + mobile + "】";
            }
            if (StringUtils.isNotEmpty(params)) {
                loggerContent += "\t请求参数列表(ง•̀_•́)ง->：【" + params + "】";
            }
            logger.error(loggerContent);

//            logger.error("ε=ε=ε=ε=ε=ε=┌(;￣◇￣)┘          切面处理请求错误！ IP信息(ง•̀_•́)ง->： 【{}】 " +
//                            "URI信息(ง•̀_•́)ง->：【{}】 请求映射控制类(ง•̀_•́)ง->：【{}】 " +
//                            "请求方法(ง•̀_•́)ง->：【{}】 请求设备(ง•̀_•́)ง->：【{}】 " +
//                            "请求参数列表(ง•̀_•́)ง->：【{}】", ip, URI, clazzName, mobile, methodName,
//                    params);
            throw throwable;
        }
        R<?> resultR = (R<?>) result;
        String type = resultR.getType();
        int code = resultR.getCode();
        Object data = resultR.getData();
        String message = resultR.getMessage();
        //请求操作成功
        String resultJsonString = "";
        if (data != null) {
            if (data instanceof HttpServletResponse) {
                resultJsonString = StringUtils.tojsonstring(data);
            } else {
                resultJsonString = String.valueOf(data);
            }
        }
        //记录请求完成执行时间：
        long endTime = System.currentTimeMillis();
        long time = endTime - beginTime;
        //删除返回结果中的换行
        resultJsonString = resultJsonString.replace("\n", "");
        String[] passMethod = SystemConstant.PASS_METHOD;
        if (Arrays.asList(passMethod).contains(methodName)) {
            return result;
        }
        //记录日志
        String loggerContent = "请求操作成功！";
        loggerContent += "\t请求耗时：【" + time + "】毫秒";
        if (StringUtils.isNotEmpty(method)) {
            loggerContent += "\t请求方式(◍'౪`◍)ﾉﾞ->： 【" + method + "】";
        }
        if (StringUtils.isNotEmpty(ip)) {
            loggerContent += "\tIP信息(◍'౪`◍)ﾉﾞ->： 【" + ip + "】";
        }
        if (StringUtils.isNotEmpty(uri)) {
            loggerContent += "\tURI信息(◍'౪`◍)ﾉﾞ->： 【" + uri + "】";
        }
        if (StringUtils.isNotEmpty(clazzName)) {
            loggerContent += "\t请求映射控制类(◍'౪`◍)ﾉﾞ->：【" + clazzName + "】";
        }
        if (StringUtils.isNotEmpty(methodName)) {
            loggerContent += "\t请求方法(◍'౪`◍)ﾉﾞ->：【" + methodName + "】";
        }
        if (StringUtils.isNotEmpty(mobile)) {
            loggerContent += "\t请求设备类型(ง•̀_•́)ง->：【" + mobile + "】";
        }
        if (StringUtils.isNotEmpty(params)) {
            loggerContent += "\t请求参数列表(◍'౪`◍)ﾉﾞ->：【" + params + "】";
        }
        if (StringUtils.isNotEmpty(resultJsonString)) {
            loggerContent += "\t返回值(ฅ´ω`ฅ)->：【" + resultJsonString + "】";
        }
        if (StringUtils.isNotEmpty(code)) {
            loggerContent += "\t返回状态值(ฅ´ω`ฅ)->：【" + code + "】";
        }
        if (StringUtils.isNotEmpty(message)) {
            loggerContent += "\t返回提示◔ ‸◔->：【" + message + "】";
        }
        if (StringUtils.isNotEmpty(type)) {
            loggerContent += "\t返回类型 ≖‿≖✧->：【" + type + "】";
        }
        logger.info(loggerContent);

//        logger.info("请求操作成功！ 请求耗时：【{}】毫秒  请求方式(◍'౪`◍)ﾉﾞ->： 【{}】 " +
//                        "IP信息(◍'౪`◍)ﾉﾞ->： 【{}】  URI信息(◍'౪`◍)ﾉﾞ->：【{}】 " +
//                        "请求映射控制类(◍'౪`◍)ﾉﾞ->：【{}】 请求方法(◍'౪`◍)ﾉﾞ->：【{}】 " +
//                        "请求设备(ง•̀_•́)ง->：【{}】 请求参数列表(◍'౪`◍)ﾉﾞ->：【{}】 " +
//                        "返回值(ฅ´ω`ฅ)->：【{}】 " + "返回状态值(ฅ´ω`ฅ)->：【{}】 " +
//                        "返回提示◔ ‸◔->：【{}】 " + "返回提示类型 ≖‿≖✧->：【{}】 ", time, method, ip, URI, clazzName,
//                methodName, mobile, params, resultJsonString, code, message, type);
        Log log = new Log();
        try {
            //操作用户
            String account = (String) StpUtil.getLoginId();
            log.setAccount(account);
            //请求方式
            log.setMethod(method);
            //URI 请求接口
            log.setUri(uri);
            //URL 请求地址
            log.setUrl(url);
            //请求来源
            log.setInnerIp(innerIp);
            //设备来源
            log.setMobile(mobile);
            //请求映射控制类
            log.setControlClass(clazzName);
            //ip
            log.setIp(ip);
            //请求参数
            log.setParams(params.toString());
            //执行时间
            log.setTime((double) time);
            //创建时间
            log.setCreateDate(LocalDateTime.now());
            //返回状态
            log.setCode(code);
            //返回提示
            log.setMessage(message);
            //返回提示类型
            log.setType(type);
            //返回结果
            int maxLen = SystemConstant.RESULT_MAX_LEN;
            if (resultJsonString.length() > maxLen) {
                resultJsonString = resultJsonString.substring(0, maxLen - 10000) + "..";
            }
            log.setResult(resultJsonString);
            //操作系统
            log.setOs(OSUtils.getOs(request));
            //浏览器信息
            log.setBrowser(BrowserUtils.getBrowser(request));
            //方法名称
            String methodApi = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(ApiOperation.class).value();
            log.setMethodApi(methodApi);
            //类名称
            String classApi = pjp.getThis().getClass().getAnnotation(Api.class).value();
            log.setClassApi(classApi);

            requestLogAspect.logService.insertLog(log);
        } catch (Exception ignored) {
        }
        return result;

    }

    /**
     * 权限拦截 - 弃用
     */
    public void preHandle(ProceedingJoinPoint pjp) throws Exception {
        PermInter permInter = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(PermInter.class);
        //如果没有添加权限注解则直接跳过允许访问
        if (permInter == null) {
            return;
        }
        //获取注解中的值
        String perm = permInter.perm();
        String account = (String) StpUtil.getLoginId();
        List<String> list;
        String key = RedisConstant.GET_LIST_PERM + account;
        if (redisUtils.noHasKey(key)) {
            permServiceImpl.createUserPermList(account);
        }


        list = redisUtils.get(key, String.class);
        if (StringUtils.isNotEmpty(list) && list.contains(perm)) {
            return;
        }
        throw new Exception("您没有 '" + permInter.name() + "' 权限！");
    }

}