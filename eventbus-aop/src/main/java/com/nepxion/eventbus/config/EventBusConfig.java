package com.nepxion.eventbus.config;

/**
 * <p>Title: Nepxion Permission</p>
 * <p>Description: Nepxion Permission</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nepxion.eventbus.aop.EventBeanPostProcessor;
import com.nepxion.eventbus.core.EventControllerFactory;
import com.nepxion.eventbus.thread.ThreadPoolFactory;

@Configuration
public class EventBusConfig {
    static {
        System.out.println("");
        System.out.println("╔═══╗        ╔╗╔══╗");
        System.out.println("║╔══╝       ╔╝╚╣╔╗║");
        System.out.println("║╚══╦╗╔╦══╦═╬╗╔╣╚╝╚╦╗╔╦══╗");
        System.out.println("║╔══╣╚╝║║═╣╔╗╣║║╔═╗║║║║══╣");
        System.out.println("║╚══╬╗╔╣║═╣║║║╚╣╚═╝║╚╝╠══║");
        System.out.println("╚═══╝╚╝╚══╩╝╚╩═╩═══╩══╩══╝");
        System.out.println("Nepxion EventBus  v1.0.6");
        System.out.println("");
    }

    @Bean
    public ThreadPoolFactory threadPoolFactory() {
        return new ThreadPoolFactory();
    }

    @Bean
    public EventControllerFactory eventControllerFactory() {
        return new EventControllerFactory();
    }

    @Bean
    public EventBeanPostProcessor eventBeanPostProcessor() {
        return new EventBeanPostProcessor();
    }
}