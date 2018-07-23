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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.eventbus.util.NetUtil;
import com.nepxion.eventbus.util.StringUtil;

public class ThreadPoolFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ThreadPoolFactory.class);

    private volatile Map<String, ThreadPoolExecutor> threadPoolExecutorMap = new ConcurrentHashMap<String, ThreadPoolExecutor>();
    private ThreadPoolExecutor threadPoolExecutor;

    private ThreadCustomization threadCustomization;
    private ThreadParameter threadParameter;

    public ThreadPoolFactory(ThreadCustomization threadCustomization, ThreadParameter threadParameter) {
        this.threadCustomization = threadCustomization;
        this.threadParameter = threadParameter;
    }

    public ThreadPoolExecutor getThreadPoolExecutor(String threadPoolName) {
        boolean threadPoolMultiMode = threadCustomization.isThreadPoolMultiMode();
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
        String threadPoolSharedName = threadCustomization.getThreadPoolSharedName();
        if (StringUtils.isEmpty(threadPoolSharedName)) {
            threadPoolSharedName = ThreadConstant.DEFAULT_THREADPOOL_SHARED_NAME;
        }

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
        boolean threadPoolNameIPShown = threadCustomization.isThreadPoolNameIPShown();

        return threadPoolNameIPShown ? StringUtil.firstLetterToUpper(threadPoolName) + "-" + NetUtil.getLocalHost() + "-thread" : StringUtil.firstLetterToUpper(threadPoolName) + "-thread";
    }

    private ThreadPoolExecutor createThreadPoolExecutor(String threadPoolName) {
        boolean threadPoolNameCustomized = threadCustomization.isThreadPoolNameCustomized();

        return threadPoolNameCustomized ? createThreadPoolExecutor(threadPoolName, threadParameter) : createThreadPoolExecutor(threadParameter);
    }

    public static ThreadPoolExecutor createThreadPoolExecutor(String threadPoolName, ThreadParameter threadParameter) {
        int corePoolSize = threadParameter.getThreadPoolCorePoolSize();
        int maximumPoolSize = threadParameter.getThreadPoolMaximumPoolSize();
        long keepAliveTime = threadParameter.getThreadPoolKeepAliveTime();
        boolean allowCoreThreadTimeout = threadParameter.isThreadPoolAllowCoreThreadTimeout();
        String queue = threadParameter.getThreadPoolQueue();
        int queueCapacity = threadParameter.getThreadPoolQueueCapacity();
        String rejectedPolicy = threadParameter.getThreadPoolRejectedPolicy();

        LOG.info("Thread pool executor is created, threadPoolName={}, threadParameter={}", threadPoolName, threadParameter);

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

    public static ThreadPoolExecutor createThreadPoolExecutor(ThreadParameter threadParameter) {
        int corePoolSize = threadParameter.getThreadPoolCorePoolSize();
        int maximumPoolSize = threadParameter.getThreadPoolMaximumPoolSize();
        long keepAliveTime = threadParameter.getThreadPoolKeepAliveTime();
        boolean allowCoreThreadTimeout = threadParameter.isThreadPoolAllowCoreThreadTimeout();
        String queue = threadParameter.getThreadPoolQueue();
        int queueCapacity = threadParameter.getThreadPoolQueueCapacity();
        String rejectedPolicy = threadParameter.getThreadPoolRejectedPolicy();

        LOG.info("Thread pool executor is created, threadParameter={}", threadParameter);

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

    public void shutdown() {
        if (threadPoolExecutor != null && !threadPoolExecutor.isShutdown()) {
            LOG.info("Shutting down thread pool executor [{}]...", threadPoolExecutor);

            threadPoolExecutor.shutdown();
        }

        for (Map.Entry<String, ThreadPoolExecutor> entry : threadPoolExecutorMap.entrySet()) {
            ThreadPoolExecutor executor = entry.getValue();

            if (executor != null && !executor.isShutdown()) {
                LOG.info("Shutting down thread pool executor [{}] ...", threadPoolExecutor);

                executor.shutdown();
            }
        }
    }
}