package com.nepxion.eventbus.aop;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.nepxion.eventbus.annotation.EnableEventBus;
import com.nepxion.eventbus.core.EventControllerFactory;

@Component("eventBeanPostProcessor")
public class EventBeanPostProcessor implements BeanPostProcessor {
    @Autowired
    private EventControllerFactory eventControllerFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(EnableEventBus.class)) {
            EnableEventBus eventBusAnnotation = bean.getClass().getAnnotation(EnableEventBus.class);
            String identifier = eventBusAnnotation.identifier();
            boolean async = eventBusAnnotation.async();

            eventControllerFactory.getController(identifier, async).register(bean);
        }

        return bean;
    }
}
