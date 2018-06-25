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

public interface EventController {
    void register(Object object);

    void unregister(Object object);
    
    void post(Object event);
    
    void post(Collection<? extends Object> event);
    
    void postEvent(Event event);

    void postEvent(Collection<? extends Event> events);
}