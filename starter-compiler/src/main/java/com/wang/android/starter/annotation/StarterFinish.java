package com.wang.android.starter.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface StarterFinish {

    /**
     * 要监听方法的方法名
     *
     * @return
     */
    String listen();
    
}
