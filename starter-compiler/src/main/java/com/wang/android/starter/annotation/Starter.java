package com.wang.android.starter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Starter {

    /**
     * 是否只在主进程中初始化
     *
     * @return boolean
     */
    boolean mainProcessOnly() default true;

}
