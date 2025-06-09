package com.example.common.expect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface expectToExcel {
    /**
     * 是否导出
     */
    boolean expect() default true;

    /**
     * 是否对应的标题
     */
    String header() default "";

    /**
     * 是否为默认模板
     */
    boolean template() default true;
}
