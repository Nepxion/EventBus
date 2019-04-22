package com.nepxion.eventbus.configuration;

/**
 * <p>Title: Nepxion Permission</p>
 * <p>Description: Nepxion Permission</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.nepxion.banner.NepxionBanner;
import com.nepxion.eventbus.aop.EventBeanPostProcessor;
import com.nepxion.eventbus.constant.EventConstant;
import com.nepxion.eventbus.context.EventContextClosedHandler;
import com.nepxion.eventbus.core.EventControllerFactory;
import com.nepxion.eventbus.thread.ThreadPoolFactory;
import com.nepxion.eventbus.thread.constant.ThreadConstant;
import com.nepxion.eventbus.thread.entity.ThreadCustomization;
import com.nepxion.eventbus.thread.entity.ThreadParameter;
import com.taobao.text.Color;

@Configuration
public class EventConfiguration {
    static {
        /*String bannerShown = System.getProperty(BannerConstant.BANNER_SHOWN, "true");
        if (Boolean.valueOf(bannerShown)) {
            System.out.println("");
            System.out.println("╔═══╗        ╔╗╔══╗");
            System.out.println("║╔══╝       ╔╝╚╣╔╗║");
            System.out.println("║╚══╦╗╔╦══╦═╬╗╔╣╚╝╚╦╗╔╦══╗");
            System.out.println("║╔══╣╚╝║║═╣╔╗╣║║╔═╗║║║║══╣");
            System.out.println("║╚══╬╗╔╣║═╣║║║╚╣╚═╝║╚╝╠══║");
            System.out.println("╚═══╝╚╝╚══╩╝╚╩═╩═══╩══╩══╝");
            System.out.println("Nepxion EventBus  v" + EventConstant.EVENTBUS_VERSION);
            System.out.println("");
        }*/

        LogoBanner logoBanner = new LogoBanner(EventConfiguration.class, "/com/nepxion/eventbus/resource/logo.txt", "Welcome to Nepxion", 8, 5, new Color[] { Color.red, Color.green, Color.cyan, Color.blue, Color.yellow, Color.magenta, Color.red, Color.green }, true);

        NepxionBanner.show(logoBanner, new Description(BannerConstant.VERSION + ":", EventConstant.EVENTBUS_VERSION, 0, 1), new Description(BannerConstant.GITHUB + ":", BannerConstant.NEPXION_GITHUB + "/EventBus", 0, 1));
    }

    @Value("${" + ThreadConstant.THREAD_POOL_MULTI_MODE + ":false}")
    private boolean threadPoolMultiMode;

    @Value("${" + ThreadConstant.THREAD_POOL_SHARED_NAME + ":EventBus}")
    private String threadPoolSharedName;

    @Value("${" + ThreadConstant.THREAD_POOL_NAME_CUSTOMIZED + ":true}")
    private boolean threadPoolNameCustomized;

    @Value("${" + ThreadConstant.THREAD_POOL_NAME_IP_SHOWN + ":true}")
    private boolean threadPoolNameIPShown;

    @Value("${" + ThreadConstant.THREAD_POOL_CORE_POOL_SIZE + ":4}")
    private int threadPoolCorePoolSize;

    @Value("${" + ThreadConstant.THREAD_POOL_MAXIMUM_POOL_SIZE + ":8}")
    private int threadPoolMaximumPoolSize;

    @Value("${" + ThreadConstant.THREAD_POOL_KEEP_ALIVE_TIME + ":900000}")
    private long threadPoolKeepAliveTime;

    @Value("${" + ThreadConstant.THREAD_POOL_ALLOW_CORE_THREAD_TIMEOUT + ":false}")
    private boolean threadPoolAllowCoreThreadTimeout;

    @Value("${" + ThreadConstant.THREAD_POOL_QUEUE + ":LinkedBlockingQueue}")
    private String threadPoolQueue;

    @Value("${" + ThreadConstant.THREAD_POOL_QUEUE_CAPACITY + ":1024}")
    private int threadPoolQueueCapacity;

    @Value("${" + ThreadConstant.THREAD_POOL_REJECTED_POLICY + ":BlockingPolicyWithReport}")
    private String threadPoolRejectedPolicy;

    @Bean
    public ThreadPoolFactory threadPoolFactory() {
        ThreadCustomization threadCustomization = new ThreadCustomization();
        threadCustomization.setThreadPoolMultiMode(threadPoolMultiMode);
        threadCustomization.setThreadPoolSharedName(threadPoolSharedName);
        threadCustomization.setThreadPoolNameCustomized(threadPoolNameCustomized);
        threadCustomization.setThreadPoolNameIPShown(threadPoolNameIPShown);

        ThreadParameter threadParameter = new ThreadParameter();
        threadParameter.setThreadPoolCorePoolSize(threadPoolCorePoolSize);
        threadParameter.setThreadPoolMaximumPoolSize(threadPoolMaximumPoolSize);
        threadParameter.setThreadPoolKeepAliveTime(threadPoolKeepAliveTime);
        threadParameter.setThreadPoolAllowCoreThreadTimeout(threadPoolAllowCoreThreadTimeout);
        threadParameter.setThreadPoolQueue(threadPoolQueue);
        threadParameter.setThreadPoolQueueCapacity(threadPoolQueueCapacity);
        threadParameter.setThreadPoolRejectedPolicy(threadPoolRejectedPolicy);

        return new ThreadPoolFactory(threadCustomization, threadParameter);
    }

    @Bean
    public EventControllerFactory eventControllerFactory() {
        return new EventControllerFactory();
    }

    @Bean
    public EventBeanPostProcessor eventBeanPostProcessor() {
        return new EventBeanPostProcessor();
    }

    @Bean
    public EventContextClosedHandler eventContextClosedHandler() {
        return new EventContextClosedHandler();
    }
}