package com.nepxion.eventbus.service;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.nepxion.eventbus.annotation.EnableEventBus;
import com.nepxion.eventbus.core.Event;

@EnableEventBus(identifier = "MyService2", async = false)
@Service("myService2Impl")
public class MyService2Impl {
    private static final Logger LOG = LoggerFactory.getLogger(MyService2Impl.class);

    @Subscribe
    public void subscribe(Event event) {
        LOG.info("Event Received - {}", event);
    }
}