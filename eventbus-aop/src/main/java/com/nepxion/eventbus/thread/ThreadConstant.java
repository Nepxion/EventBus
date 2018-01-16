package com.nepxion.eventbus.thread;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

public class ThreadConstant {
    public static final int CPUS = Math.max(2, Runtime.getRuntime().availableProcessors());

    public static final String THREAD_POOL_MULTI_MODE = "threadPool.multiMode";
    public static final String THREAD_POOL_SHARED_NAME = "threadPool.sharedName";
    public static final String THREAD_POOL_NAME_CUSTOMIZED = "threadPool.nameCustomized";
    public static final String THREAD_POOL_NAME_IP_SHOWN = "threadPool.nameIPShown";
    public static final String THREAD_POOL_CORE_POOL_SIZE = "threadPool.corePoolSize";
    public static final String THREAD_POOL_MAXIMUM_POOL_SIZE = "threadPool.maximumPoolSize";
    public static final String THREAD_POOL_KEEP_ALIVE_TIME = "threadPool.keepAliveTime";
    public static final String THREAD_POOL_ALLOW_CORE_THREAD_TIMEOUT = "threadPool.allowCoreThreadTimeout";
    public static final String THREAD_POOL_QUEUE = "threadPool.queue";
    public static final String THREAD_POOL_QUEUE_CAPACITY = "threadPool.queueCapacity";
    public static final String THREAD_POOL_REJECTED_POLICY = "threadPool.rejectedPolicy";
}