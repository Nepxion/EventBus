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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nepxion.eventbus.core.Event;
import com.nepxion.eventbus.core.EventControllerFactory;

@Service
public class MyPublisher {
    private static final Logger LOG = LoggerFactory.getLogger(MyPublisher.class);

    @Autowired(required = false)
    private EventControllerFactory eventControllerFactory;

    public void publish() {
        if (eventControllerFactory != null) {
            LOG.info("发送事件...");

            // 异步模式下(默认)，子线程中收到派发的事件
            eventControllerFactory.getAsyncController().post("Sync Event String Format");

            // 同步模式下，主线程中收到派发的事件
            // 事件派发接口中eventControllerFactory.getSyncController(identifier)必须和@EnableEventBus参数保持一致，否则会收不到事件
            eventControllerFactory.getSyncController().post("Sync Event String Format");

            // 异步模式下(默认)，子线程中收到派发的事件
            eventControllerFactory.getAsyncController().post(12345L);

            // 同步模式下，主线程中收到派发的事件
            // 事件派发接口中eventControllerFactory.getSyncController(identifier)必须和@EnableEventBus参数保持一致，否则会收不到事件
            eventControllerFactory.getSyncController().post(Boolean.TRUE);

            // 异步模式下(默认)，子线程中收到派发的事件
            eventControllerFactory.getAsyncController().postEvent(new Event("Async Event"));

            // 同步模式下，主线程中收到派发的事件
            // 事件派发接口中eventControllerFactory.getSyncController(identifier)必须和@EnableEventBus参数保持一致，否则会收不到事件
            eventControllerFactory.getSyncController().postEvent(new Event("Sync Event"));
        } else {
            LOG.error("EventBus功能未开启");
        }
    }
}