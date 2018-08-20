package com.nepxion.eventbus.aop;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.nepxion.eventbus.annotation.EnableEventBus;
import com.nepxion.eventbus.constant.EventConstant;
import com.nepxion.matrix.selector.AbstractImportSelector;
import com.nepxion.matrix.selector.RelaxedPropertyResolver;

@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class EventBusImportSelector extends AbstractImportSelector<EnableEventBus> {
    @Override
    protected boolean isEnabled() {
        return new RelaxedPropertyResolver(getEnvironment()).getProperty(EventConstant.EVENTBUS_ENABLED, Boolean.class, Boolean.TRUE);        
    }
}