package com.wang.android.starter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface StarterMethod {

    /**
     * 初始化优先级 [0-99] 数值越大优先级越高
     *
     * @return
     */
    int priority() default 99;

    //是否需要同步初始化
    boolean isSync() default true;

    //是否可以延迟初始化
    boolean isDelay() default false;

}
