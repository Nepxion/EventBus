package com.nepxion.eventbus.example.service;

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
import com.nepxion.eventbus.annotation.EventBus;
import com.nepxion.eventbus.core.Event;

@EventBus(async = false)
@Service
public class MySubscriber2 {
    private static final Logger LOG = LoggerFactory.getLogger(MySubscriber2.class);

    @Subscribe
    public void subscribe(Event event) {
        LOG.info("子线程接收异步事件 - {}", event);
    }
}