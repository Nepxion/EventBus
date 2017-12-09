# Matrix EventBus
[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

基于Google Guava机制定制，基于注解方式轻松实现事件总线逻辑

## 介绍

    1. 实现基于@EnableEventBus注解开启EventBus机制
    2. 实现异步模式下(默认)，子线程中收到派发的事件
    3. 实现批量派发事件
    4. 实现同步模式下，主线程中收到派发的事件
    5. 实现线程隔离技术，并定制化配置线程池

## 使用
示例1，异步模式(默认)下接收事件
```java
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

@EnableEventBus
@Service("myService1Impl")
public class MyService1Impl {
    private static final Logger LOG = LoggerFactory.getLogger(MyService1Impl.class);

    @Subscribe
    public void subscribe(Event event) {
        LOG.info("Event Received - {}", event);
    }
}
```

示例2，同步模式下接收事件
```java
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
```

示例3，派发事件
```java
package com.nepxion.eventbus;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.nepxion.eventbus.context.EventContextAware;
import com.nepxion.eventbus.core.Event;
import com.nepxion.eventbus.core.EventControllerFactory;

@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.nepxion.eventbus" })
public class MyApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication.class, args);

        EventControllerFactory eventControllerFactory = EventContextAware.getBean(EventControllerFactory.class);

        for (int i = 0; i < 20; i++) {
            // 异步模式下(默认)，子线程中收到派发的事件
            eventControllerFactory.getSingletonController().post(new Event("A-" + i));

            // 同步模式下，主线程中收到派发的事件
            // 事件派发接口中eventControllerFactory.getController(identifier, async)必须和@EnableEventBus参数保持一致，否则会收不到事件
            eventControllerFactory.getController("MyService2", false).post(new Event("B-" + i));
        }
    }
}
```