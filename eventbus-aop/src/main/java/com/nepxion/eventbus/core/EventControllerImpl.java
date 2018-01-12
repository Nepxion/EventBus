package com.nepxion.eventbus.core;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Collection;

import com.google.common.eventbus.EventBus;

public class EventControllerImpl implements EventController {
    private EventBus eventBus;

    public EventControllerImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void register(Object object) {
        eventBus.register(object);
    }

    @Override
    public void unregister(Object object) {
        eventBus.unregister(object);
    }

    @Override
    public void post(Event event) {
        eventBus.post(event);
    }

    @Override
    public void post(Collection<? extends Event> events) {
        for (Event event : events) {
            eventBus.post(event);
        }
    }
}