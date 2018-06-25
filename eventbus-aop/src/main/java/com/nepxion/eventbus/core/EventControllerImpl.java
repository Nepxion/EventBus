package com.nepxion.eventbus.core;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
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
    public void post(Object event) {
        eventBus.post(event);

    }

    @Override
    public void post(Collection<? extends Object> events) {
        for (Object event : events) {
            eventBus.post(event);
        }
    }

    @Override
    public void postEvent(Event event) {
        post(event);
    }

    @Override
    public void postEvent(Collection<? extends Event> events) {
        post(events);
    }
}