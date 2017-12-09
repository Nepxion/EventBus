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
            eventControllerFactory.getSharedController().post(new Event("A-" + i));

            // 同步模式下，主线程中收到派发的事件
            // 事件派发接口中eventControllerFactory.getController(identifier, async)必须和@EnableEventBus参数保持一致，否则会收不到事件
            eventControllerFactory.getController("MyService2", false).post(new Event("B-" + i));
        }
    }
}