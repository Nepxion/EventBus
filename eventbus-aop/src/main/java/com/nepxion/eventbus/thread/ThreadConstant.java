package com.nepxion.eventbus.thread;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public class ThreadConstant {
    public static final int CPUS = Math.max(2, Runtime.getRuntime().availableProcessors());

    public static final String THREAD_POOL_MULTI_MODE = "threadPoolMultiMode";
    public static final String THREAD_POOL_SHARED_NAME = "threadPoolSharedName";
    public static final String THREAD_POOL_NAME_CUSTOMIZED = "threadPoolNameCustomized";
    public static final String THREAD_POOL_NAME_IP_SHOWN = "threadPoolNameIPShown";
    public static final String THREAD_POOL_CORE_POOL_SIZE = "threadPoolCorePoolSize";
    public static final String THREAD_POOL_MAXIMUM_POOL_SIZE = "threadPoolMaximumPoolSize";
    public static final String THREAD_POOL_KEEP_ALIVE_TIME = "threadPoolKeepAliveTime";
    public static final String THREAD_POOL_ALLOW_CORE_THREAD_TIMEOUT = "threadPoolAllowCoreThreadTimeout";
    public static final String THREAD_POOL_QUEUE = "threadPoolQueue";
    public static final String THREAD_POOL_QUEUE_CAPACITY = "threadPoolQueueCapacity";
    public static final String THREAD_POOL_REJECTED_POLICY = "threadPoolRejectedPolicy";
}