# Nepxion EventBus
[![Total lines](https://tokei.rs/b1/github/Nepxion/EventBus?category=lines)](https://tokei.rs/b1/github/Nepxion/EventBus?category=lines)  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/Nepxion/EventBus/blob/master/LICENSE)  [![Maven Central](https://img.shields.io/maven-central/v/com.nepxion/eventbus.svg?label=maven%20central)](https://search.maven.org/artifact/com.nepxion/eventbus)  [![Javadocs](http://www.javadoc.io/badge/com.nepxion/eventbus-aop.svg)](http://www.javadoc.io/doc/com.nepxion/eventbus-aop)  [![Build Status](https://travis-ci.org/Nepxion/EventBus.svg?branch=master)](https://travis-ci.org/Nepxion/EventBus)  [![Codacy Badge](https://api.codacy.com/project/badge/Grade/cee72965ac43443987e77e0ee24c15f3)](https://www.codacy.com/project/HaojunRen/EventBus/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Nepxion/EventBus&amp;utm_campaign=Badge_Grade_Dashboard)  [![Stars](https://img.shields.io/github/stars/Nepxion/EventBus.svg?label=Stars&tyle=flat&logo=GitHub)](https://github.com/Nepxion/EventBus/stargazers)  [![Stars](https://gitee.com/Nepxion/EventBus/badge/star.svg)](https://gitee.com/nepxion/EventBus/stargazers)

Nepxion EventBus是一款基于Google Guava通用事件派发机制的事件总线组件。它采用Spring Framework AOP机制，提供注解调用方式，支持异步和同步两种方式

## 请联系我
微信和公众号

![](http://nepxion.gitee.io/docs/zxing-doc/微信-1.jpg)![](http://nepxion.gitee.io/docs/zxing-doc/公众号-1.jpg)![](http://nepxion.gitee.io/docs/zxing-doc/文档-1.jpg)

## 简介
- 实现基于@EventBus注解开启EventBus机制
- 实现异步模式下(默认)，子线程中收到派发的事件，基于@EventBus(async = false)，来切换是同步还是异步
- 实现批量派发事件
- 实现同步模式下，主线程中收到派发的事件
- 实现线程隔离技术，并定制化配置线程池
- 实现事件对象的多元化，可以发布和订阅Java基本类型，也可以利用框架内置的Event类型，当然也可以使用任意自定义类型	

## 兼容
最新版本兼容
- Spring 4.x.x和Spring Boot 1.x.x
- Spring 5.x.x和Spring Boot 2.x.x

## 依赖
```xml
<dependency>
    <groupId>com.nepxion</groupId>
    <artifactId>eventbus-aop-starter</artifactId>
    <version>${eventbus.version}</version>
</dependency>
```

## 用法
```java
@SpringBootApplication
@EnableEventBus
public class MyApplication {
}
```

## 策略
- EventBus事件控制器（Controller）策略
  - 可以由单个Controller控制缺省identifier的EventBus事件（在Google Guava内部定义缺省identifier的值为'default'）。用法如下：
```java
事件发布端：
eventControllerFactory.getAsyncController().post("abc"); // 异步发送
eventControllerFactory.getSyncController().post("abc"); // 同步发送
```
```java
事件订阅端：
@EventBus // 订阅异步消息，async不指定，默认为true
public class MySubscriber {
}
@EventBus(async = false) // 订阅同步消息
public class MySubscriber {
}
```  
  - 可以由多个Controller控制不同identifier的EventBus事件。用法如下：
```java
事件发布端：
eventControllerFactory.getAsyncController(identifier).post("abc"); // 异步发送
eventControllerFactory.getSyncController(identifier).post("abc"); // 同步发送
```  
```java
事件订阅端：
@EventBus(identifier = "xyz") // 订阅异步消息，async不指定，默认为true
public class MySubscriber {
}
@EventBus(identifier = "xyz", async = false) // 订阅同步消息
public class MySubscriber {
}
```
>注意：事件发布端和订阅端的identifier一定要一致
```java
# EventBus config
# 开关配置，结合注解@EnableEventBus使用
# eventbus.enabled=true
```

- EventBus线程池（ThreadPool）策略
  - 配置如下：
线程池配置，参考application.properties，可以不需要配置，那么采取如下默认值
```java
# Thread Pool Config
# Multi thread pool，是否线程隔离。如果是，那么每个不同identifier的事件都会占用一个单独线程池，否则共享一个线程池
threadPoolMultiMode=false
# 共享线程池的名称
threadPoolSharedName=EventBus
# 是否显示自定义的线程池名
threadPoolNameCustomized=true
# CPU unit（CPU核数单位，例如在8核心CPU上，threadPoolCorePoolSize配置为2，那么最终核心线程数为16，下同）
threadPoolCorePoolSize=1
# CPU unit
threadPoolMaximumPoolSize=2
threadPoolKeepAliveTime=900000
threadPoolAllowCoreThreadTimeout=false
# LinkedBlockingQueue, ArrayBlockingQueue, SynchronousQueue
threadPoolQueue=LinkedBlockingQueue
# CPU unit (Used for LinkedBlockingQueue or ArrayBlockingQueue)
threadPoolQueueCapacity=128
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
    public void subscribe(String event) {
        LOG.info("子线程接收异步事件 - {}，String类型", event);
    }

    @Subscribe
    public void subscribe(Long event) {
        LOG.info("子线程接收异步事件 - {}，Long类型", event);
    }

    @Subscribe
    public void subscribe(Boolean event) {
        LOG.info("子线程接收异步事件 - {}，Boolean类型", event);
    }

    @Subscribe
    public void subscribe(Event event) {
        LOG.info("子线程接收异步事件 - {}，内置类型Event", event);
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
    public void subscribe(String event) {
        LOG.info("主线程接收同步事件 - {}，String类型", event);
    }

    @Subscribe
    public void subscribe(Long event) {
        LOG.info("主线程接收同步事件 - {}，Long类型", event);
    }

    @Subscribe
    public void subscribe(Boolean event) {
        LOG.info("主线程接收同步事件 - {}，Boolean类型", event);
    }

    @Subscribe
    public void subscribe(Event event) {
        LOG.info("主线程接收同步事件 - {}，内置类型Event", event);
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

import com.nepxion.eventbus.annotation.EnableEventBus;
import com.nepxion.eventbus.example.service.MyPublisher;

@SpringBootApplication
@EnableEventBus
public class MyApplication {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MyApplication.class, args);

        MyPublisher myPublisher = applicationContext.getBean(MyPublisher.class);
        myPublisher.publish();
    }
}
```

运行结果
```java
2018-06-25 13:01:02.008 INFO [main][com.nepxion.eventbus.example.service.MyPublisher:28] - 发送事件...
2018-06-25 13:01:02.015 INFO [EventBus-192.168.0.107-thread-0][com.nepxion.eventbus.example.service.MySubscriber1:27] - 子线程接收异步事件 - Sync Event String Format，String类型
2018-06-25 13:01:02.016 INFO [main][com.nepxion.eventbus.example.service.MySubscriber2:27] - 主线程接收同步事件 - Sync Event String Format，String类型
2018-06-25 13:01:02.016 INFO [main][com.nepxion.eventbus.example.service.MySubscriber2:37] - 主线程接收同步事件 - true，Boolean类型
2018-06-25 13:01:02.016 INFO [EventBus-192.168.0.107-thread-1][com.nepxion.eventbus.example.service.MySubscriber1:32] - 子线程接收异步事件 - 12345，Long类型
2018-06-25 13:01:02.017 INFO [EventBus-192.168.0.107-thread-2][com.nepxion.eventbus.example.service.MySubscriber1:42] - 子线程接收异步事件 - com.nepxion.eventbus.core.Event@67ca8c1f[
  source=Async Event
]，内置类型Event
2018-06-25 13:01:02.017 INFO [main][com.nepxion.eventbus.example.service.MySubscriber2:42] - 主线程接收同步事件 - com.nepxion.eventbus.core.Event@1bcf67e8[
  source=Sync Event
]，内置类型Event
```

## Star走势图
[![Stargazers over time](https://starchart.cc/Nepxion/EventBus.svg)](https://starchart.cc/Nepxion/EventBus)