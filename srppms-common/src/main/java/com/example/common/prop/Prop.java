package com.example.common.prop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Prop {
    /**
     * 是否为表头
     */
    boolean isProp() default true;

    /**
     * 表示码
     */
    String prop() default "";

    /**
     * 名称
     */
    String label() default "";

    /**
     * 排序
     */
    int index() default -1;
}
