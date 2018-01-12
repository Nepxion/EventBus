package com.nepxion.eventbus.service;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.nepxion.eventbus.annotation.EnableEventBus;
import com.nepxion.eventbus.core.Event;

@EnableEventBus
@Service("myService1Impl")
public class MyService1Impl {
    private static final Logger LOG = LoggerFactory.getLogger(MyService1Impl.class);

    @Subscribe
    public void subscribe(Event event) {
        LOG.info("Event Received - {}", event);
    }
}