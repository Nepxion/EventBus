# EventBus
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

@EnableEventBus(async = false)
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
```javapackage com.nepxion.eventbus;

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

        // 异步模式下(默认)，子线程中收到派发的事件
        eventControllerFactory.getAsyncController().post(new Event("Async Event"));

        // 同步模式下，主线程中收到派发的事件
        // 事件派发接口中eventControllerFactory.getSyncController(identifier)必须和@EnableEventBus参数保持一致，否则会收不到事件
        eventControllerFactory.getSyncController().post(new Event("Sync Event"));
    }
}
```

线程池配置，参考application.properties
```java
# Thread Pool Config
# Multi thread pool，是否线程隔离，如果是那么每个不同类型的事件都会占用一个单独线程池，否则共享一个线程池
threadPoolMultiMode=false
# 共享线程池的名称
threadPoolSharedName=EventBus
# 是否显示自定义的线程池名
threadPoolNameCustomized=true
# CPU unit
threadPoolCorePoolSize=4
# CPU unit
threadPoolMaximumPoolSize=8
threadPoolKeepAliveTime=900000
threadPoolAllowCoreThreadTimeout=false
# LinkedBlockingQueue, ArrayBlockingQueue, SynchronousQueue
threadPoolQueue=LinkedBlockingQueue
# CPU unit (Used for LinkedBlockingQueue or ArrayBlockingQueue)
threadPoolQueueCapacity=1024
# BlockingPolicyWithReport, CallerRunsPolicyWithReport, AbortPolicyWithReport, RejectedPolicyWithReport, DiscardedPolicyWithReport
threadPoolRejectedPolicy=BlockingPolicyWithReport
```