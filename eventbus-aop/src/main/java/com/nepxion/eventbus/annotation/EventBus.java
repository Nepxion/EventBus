package com.nepxion.eventbus.annotation;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.nepxion.eventbus.constant.EventConstant;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EventBus {
    /**
     * 事件标识
     * @return identifier
     */
    String identifier() default EventConstant.SHARED_CONTROLLER;

    /**
     * 事件是否采用异步执行
     * @return boolean
     */
    boolean async() default true;
}