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