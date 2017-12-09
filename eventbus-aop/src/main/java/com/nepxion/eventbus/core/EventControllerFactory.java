package com.nepxion.eventbus.core;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.nepxion.eventbus.constant.EventConstant;
import com.nepxion.eventbus.thread.ThreadPoolFactory;

@Component("eventControllerFactory")
public final class EventControllerFactory {
    @Autowired
    private ThreadPoolFactory threadPoolFactory;

    private volatile Map<String, EventController> syncControllerMap = new ConcurrentHashMap<String, EventController>();
    private volatile Map<String, EventController> asyncControllerMap = new ConcurrentHashMap<String, EventController>();

    private EventControllerFactory() {

    }

    public EventController getSharedController() {
        return getSharedController(true);
    }

    public EventController getSharedController(boolean async) {
        return getSharedController(async ? EventType.ASYNC : EventType.SYNC);
    }

    public EventController getSharedController(EventType type) {
        return getController(EventConstant.SHARED_CONTROLLER, type);
    }

    public EventController getController(String identifier, boolean async) {
        return getController(identifier, async ? EventType.ASYNC : EventType.SYNC);
    }

    public EventController getController(String identifier, EventType type) {
        switch (type) {
            case SYNC:
                EventController syncEventController = syncControllerMap.get(identifier);
                if (syncEventController == null) {
                    EventController newEventController = createSyncController(identifier);
                    syncEventController = syncControllerMap.putIfAbsent(identifier, newEventController);
                    if (syncEventController == null) {
                        syncEventController = newEventController;
                    }
                }

                return syncEventController;
            case ASYNC:
                EventController asyncEventController = asyncControllerMap.get(identifier);
                if (asyncEventController == null) {
                    EventController newEventController = createAsyncController(identifier, threadPoolFactory.getThreadPoolExecutor(identifier));
                    asyncEventController = asyncControllerMap.putIfAbsent(identifier, newEventController);
                    if (asyncEventController == null) {
                        asyncEventController = newEventController;
                    }
                }

                return asyncEventController;
        }

        return null;
    }

    public EventController createSyncController() {
        return new EventControllerImpl(new EventBus());
    }

    public EventController createSyncController(String identifier) {
        return new EventControllerImpl(new EventBus(identifier));
    }

    public EventController createSyncController(SubscriberExceptionHandler subscriberExceptionHandler) {
        return new EventControllerImpl(new EventBus(subscriberExceptionHandler));
    }

    public EventController createAsyncController(String identifier, Executor executor) {
        return new EventControllerImpl(new AsyncEventBus(identifier, executor));
    }

    public EventController createAsyncController(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler) {
        return new EventControllerImpl(new AsyncEventBus(executor, subscriberExceptionHandler));
    }

    public EventController createAsyncController(Executor executor) {
        return new EventControllerImpl(new AsyncEventBus(executor));
    }
}