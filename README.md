# Nepxion EventBus
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/Nepxion/EventBus/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.nepxion/eventbus.svg?label=maven%20central)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.nepxion%22%20AND%20eventbus)
[![Javadocs](http://www.javadoc.io/badge/com.nepxion/eventbus-aop.svg)](http://www.javadoc.io/doc/com.nepxion/eventbus-aop)
[![Build Status](https://travis-ci.org/Nepxion/EventBus.svg?branch=master)](https://travis-ci.org/Nepxion/EventBus)

Nepxion EventBus是一款基于Google Guava通用事件派发机制的事件总线组件，它采用Nepxion Matrix AOP框架进行切面架构，提供注解调用方式，支持异步和同步两种方式

## 简介
支持如下功能

    1. 实现基于@EventBus注解开启EventBus机制
    2. 实现异步模式下(默认)，子线程中收到派发的事件
    3. 实现批量派发事件
    4. 实现同步模式下，主线程中收到派发的事件
    5. 实现线程隔离技术，并定制化配置线程池

## 依赖
```xml
<dependency>
  <groupId>com.nepxion</groupId>
  <artifactId>eventbus-aop</artifactId>
  <version>${eventbus.version}</version>
</dependency>
```

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

@EventBus
@Service
public class MySubscriber1 {
    private static final Logger LOG = LoggerFactory.getLogger(MySubscriber1.class);

    @Subscribe
    public void subscribe(Event event) {
        LOG.info("主线程接收同步事件 - {}", event);
    }
}
```

调用入口2，同步模式下接收事件
```java
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
```

调用入口3，派发事件
```java
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

    @Autowired
    private EventControllerFactory eventControllerFactory;

    public void publish() {
        LOG.info("发送事件...");
        
        // 异步模式下(默认)，子线程中收到派发的事件
        eventControllerFactory.getAsyncController().post(new Event("Async Event"));

        // 同步模式下，主线程中收到派发的事件
        // 事件派发接口中eventControllerFactory.getSyncController(identifier)必须和@EnableEventBus参数保持一致，否则会收不到事件
        eventControllerFactory.getSyncController().post(new Event("Sync Event"));
    }
}
```

主入口
```java
package com.nepxion.eventbus.example;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.nepxion.eventbus.example.service.MyPublisher;

@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MyApplication.class, args);

        MyPublisher myPublisher = applicationContext.getBean(MyPublisher.class);
        myPublisher.publish();
    }
}
```