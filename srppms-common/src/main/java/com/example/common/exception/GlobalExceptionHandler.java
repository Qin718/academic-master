package com.example.common.exception;


import cn.dev33.satoken.exception.NotLoginException;
import com.example.common.response.R;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * SaToken全局异常拦截
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常拦截，鉴权失败不会报错，会返回给前端报错原因
     */
    @ExceptionHandler
    public R<String> handlerException(Exception e) {
        return R.warn(e.getMessage());
    }

    /**
     * 数据库语句错误
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    public R<String> sqlException(Exception e) {
        String message = e.getMessage();
        String[] split = message.split("###");
        split = split[2].replace("]", "").split("\\\\");
        String xml = split[split.length - 1].replace("\n", "");
        String sql = message.split("### Cause: ")[0].split("### SQL: ")[1].replace("\n", "");
        while (sql.contains("  ")) {
            sql = sql.replace("  ", " ");
        }
        System.out.println("==========================");
        System.out.println("SQL语句发生错误");
        System.out.println("xml: " + xml);
        System.out.println("sql：" + sql);
        System.out.println("==========================");
        return R.warn("SQL语句发生错误");
    }

    /**
     * token失效
     */
    @ExceptionHandler(NotLoginException.class)
    public R<String> handlerNotLoginException() {
        return R.token();
    }


    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public R<String> nullPointerException() {
        return R.NullPointer();
    }

    /**
     * 手动抛出的异常
     */
    @ExceptionHandler(UndeclaredThrowableException.class)
    public R<String> undeclaredThrowableException(Exception e) {
        String message = e.getCause().getMessage();
        //没有权限
        if (message.contains("权限")) {
            return R.permInter(message);
        }
        //数据检查异常
        if (message.contains("数据检查")) {
            return R.fail(message);
        }
        //其他情况
        //....

        return R.fail(message);
    }

    /**
     * 请求缺少参数
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<String> handleHttpMessageNotReadableException(){
        return  R.missParam();
    }

}

