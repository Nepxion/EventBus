package com.nepxion.eventbus.thread;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;

import com.nepxion.eventbus.util.HostUtil;
import com.nepxion.eventbus.util.StringUtil;

public class ThreadPoolFactory implements DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(ThreadPoolFactory.class);

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

    private volatile Map<String, ThreadPoolExecutor> threadPoolExecutorMap = new ConcurrentHashMap<String, ThreadPoolExecutor>();

    private ThreadPoolExecutor threadPoolExecutor;

    public ThreadPoolExecutor getThreadPoolExecutor(String threadPoolName) {
        String poolName = createThreadPoolName(threadPoolName);

        if (threadPoolMultiMode) {
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(poolName);
            if (threadPoolExecutor == null) {
                ThreadPoolExecutor newThreadPoolExecutor = createThreadPoolExecutor(poolName);
                threadPoolExecutor = threadPoolExecutorMap.putIfAbsent(poolName, newThreadPoolExecutor);
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = newThreadPoolExecutor;
                }
            }

            return threadPoolExecutor;
        } else {
            return createSharedThreadPoolExecutor();
        }
    }

    private ThreadPoolExecutor createSharedThreadPoolExecutor() {
        String poolName = createThreadPoolName(threadPoolSharedName);

        if (threadPoolExecutor == null) {
            synchronized (ThreadPoolFactory.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = createThreadPoolExecutor(poolName);
                }
            }
        }

        return threadPoolExecutor;
    }

    private String createThreadPoolName(String threadPoolName) {
        return threadPoolNameIPShown ? StringUtil.firstLetterToUpper(threadPoolName) + "-" + HostUtil.getLocalhost() + "-thread" : StringUtil.firstLetterToUpper(threadPoolName) + "-thread";
    }

    private ThreadPoolExecutor createThreadPoolExecutor(String threadPoolName) {
        return threadPoolNameCustomized ?
                createThreadPoolExecutor(threadPoolName, threadPoolCorePoolSize, threadPoolMaximumPoolSize, threadPoolKeepAliveTime, threadPoolAllowCoreThreadTimeout, threadPoolQueue, threadPoolQueueCapacity, threadPoolRejectedPolicy) :
                createThreadPoolExecutor(threadPoolCorePoolSize, threadPoolMaximumPoolSize, threadPoolKeepAliveTime, threadPoolAllowCoreThreadTimeout, threadPoolQueue, threadPoolQueueCapacity, threadPoolRejectedPolicy);
    }

    public static ThreadPoolExecutor createThreadPoolExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, boolean allowCoreThreadTimeout, String queue, int queueCapacity, String rejectedPolicy) {
        LOG.info("Thread pool executor is created, threadPoolName={}, corePoolSize={}, maximumPoolSize={}, keepAliveTime={}, allowCoreThreadTimeout={}, queue={}, queueCapacity={}, rejectedPolicy={}", threadPoolName, corePoolSize, maximumPoolSize, keepAliveTime, allowCoreThreadTimeout, queue, queueCapacity, rejectedPolicy);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                createBlockingQueue(queue, queueCapacity),
                new ThreadFactory() {
                    private AtomicInteger number = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable runnable) {
                        return new Thread(runnable, threadPoolName + "-" + number.getAndIncrement());
                    }
                },
                createRejectedPolicy(rejectedPolicy));
        threadPoolExecutor.allowCoreThreadTimeOut(allowCoreThreadTimeout);

        return threadPoolExecutor;
    }

    public static ThreadPoolExecutor createThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, boolean allowCoreThreadTimeout, String queue, int queueCapacity, String rejectedPolicy) {
        LOG.info("Thread pool executor is created, corePoolSize={}, maximumPoolSize={}, keepAliveTime={}, allowCoreThreadTimeout={}, queue={}, queueCapacity={}, rejectedPolicy={}", corePoolSize, maximumPoolSize, keepAliveTime, allowCoreThreadTimeout, queue, queueCapacity, rejectedPolicy);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                createBlockingQueue(queue, queueCapacity),
                createRejectedPolicy(rejectedPolicy));
        threadPoolExecutor.allowCoreThreadTimeOut(allowCoreThreadTimeout);

        return threadPoolExecutor;
    }

    private static BlockingQueue<Runnable> createBlockingQueue(String queue, int queueCapacity) {
        ThreadQueueType queueType = ThreadQueueType.fromString(queue);

        int capacity = ThreadConstant.CPUS * queueCapacity;

        switch (queueType) {
            case LINKED_BLOCKING_QUEUE:
                return new LinkedBlockingQueue<Runnable>(capacity);
            case ARRAY_BLOCKING_QUEUE:
                return new ArrayBlockingQueue<Runnable>(capacity);
            case SYNCHRONOUS_QUEUE:
                return new SynchronousQueue<Runnable>();
        }

        return null;
    }

    private static RejectedExecutionHandler createRejectedPolicy(String rejectedPolicy) {
        ThreadRejectedPolicyType rejectedPolicyType = ThreadRejectedPolicyType.fromString(rejectedPolicy);

        switch (rejectedPolicyType) {
            case BLOCKING_POLICY_WITH_REPORT:
                return new BlockingPolicyWithReport();
            case CALLER_RUNS_POLICY_WITH_REPORT:
                return new CallerRunsPolicyWithReport();
            case ABORT_POLICY_WITH_REPORT:
                return new AbortPolicyWithReport();
            case REJECTED_POLICY_WITH_REPORT:
                return new RejectedPolicyWithReport();
            case DISCARDED_POLICY_WITH_REPORT:
                return new DiscardedPolicyWithReport();
        }

        return null;
    }

    @Override
    public void destroy() throws Exception {
        if (threadPoolExecutor != null && !threadPoolExecutor.isShutdown()) {
            LOG.info("Shutting down thread pool executor [{}]...", threadPoolExecutor);

            threadPoolExecutor.shutdown();
        }

        for (Map.Entry<String, ThreadPoolExecutor> entry : threadPoolExecutorMap.entrySet()) {
            ThreadPoolExecutor executor = entry.getValue();

            if (executor != null && !executor.isShutdown()) {
                LOG.info("Shutting down thread pool executor [{}]...", threadPoolExecutor);

                executor.shutdown();
            }
        }
    }
}