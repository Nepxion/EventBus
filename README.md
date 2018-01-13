# Nepxion EventBus
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/Nepxion/EventBus/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.nepxion/eventbus.svg?label=maven%20central)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.nepxion%22%20AND%20eventbus)
[![Javadocs](http://www.javadoc.io/badge/com.nepxion/eventbus.svg)](http://www.javadoc.io/doc/com.nepxion/eventbus)
[![Build Status](https://travis-ci.org/Nepxion/EventBus.svg?branch=master)](https://travis-ci.org/Nepxion/EventBus)

Nepxion EventBus是一款基于Google Guava通用事件派发机制的事件总线组件，它采用Nepxion Matrix AOP框架进行切面架构，提供注解调用方式，支持异步和同步两种方式

## 简介
支持如下功能

    1. 实现基于@EnableEventBus注解开启EventBus机制
    2. 实现异步模式下(默认)，子线程中收到派发的事件
    3. 实现批量派发事件
    4. 实现同步模式下，主线程中收到派发的事件
    5. 实现线程隔离技术，并定制化配置线程池

## 配置
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

## 示例
调用入口1，异步模式(默认)下接收事件
```java
package com.nepxion.eventbus.service;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
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

调用入口2，同步模式下接收事件
```java
package com.nepxion.eventbus.service;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
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

调用入口3，派发事件
```javapackage com.nepxion.eventbus;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
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

运行结果
```java
2017-12-24 15:09:28.910 INFO [EventBus-192.168.1.3-thread-0][com.nepxion.eventbus.service.MyService1Impl:28] - Event Received - com.nepxion.eventbus.core.Event@621adb11[
  source=Async Event
]
2017-12-24 15:09:28.910 INFO [main][com.nepxion.eventbus.service.MyService2Impl:28] - Event Received - com.nepxion.eventbus.core.Event@6de30571[
  source=Sync Event
]
```
