package com.nepxion.eventbus.core;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Collection;

public interface EventController {
    void register(Object object);

    void unregister(Object object);

    void post(Event event);

    void post(Collection<? extends Event> events);
}