package com.nepxion.eventbus.thread;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ThreadParameter implements Serializable {
    private static final long serialVersionUID = 6869706244434951605L;

    private int threadPoolCorePoolSize = 4;
    private int threadPoolMaximumPoolSize = 8;
    private long threadPoolKeepAliveTime = 900000;
    private boolean threadPoolAllowCoreThreadTimeout = false;
    private String threadPoolQueue = "LinkedBlockingQueue";
    private int threadPoolQueueCapacity = 1024;
    private String threadPoolRejectedPolicy = "BlockingPolicyWithReport";

    public int getThreadPoolCorePoolSize() {
        return threadPoolCorePoolSize;
    }

    public void setThreadPoolCorePoolSize(int threadPoolCorePoolSize) {
        this.threadPoolCorePoolSize = threadPoolCorePoolSize;
    }

    public int getThreadPoolMaximumPoolSize() {
        return threadPoolMaximumPoolSize;
    }

    public void setThreadPoolMaximumPoolSize(int threadPoolMaximumPoolSize) {
        this.threadPoolMaximumPoolSize = threadPoolMaximumPoolSize;
    }

    public long getThreadPoolKeepAliveTime() {
        return threadPoolKeepAliveTime;
    }

    public void setThreadPoolKeepAliveTime(long threadPoolKeepAliveTime) {
        this.threadPoolKeepAliveTime = threadPoolKeepAliveTime;
    }

    public boolean isThreadPoolAllowCoreThreadTimeout() {
        return threadPoolAllowCoreThreadTimeout;
    }

    public void setThreadPoolAllowCoreThreadTimeout(boolean threadPoolAllowCoreThreadTimeout) {
        this.threadPoolAllowCoreThreadTimeout = threadPoolAllowCoreThreadTimeout;
    }

    public String getThreadPoolQueue() {
        return threadPoolQueue;
    }

    public void setThreadPoolQueue(String threadPoolQueue) {
        this.threadPoolQueue = threadPoolQueue;
    }

    public int getThreadPoolQueueCapacity() {
        return threadPoolQueueCapacity;
    }

    public void setThreadPoolQueueCapacity(int threadPoolQueueCapacity) {
        this.threadPoolQueueCapacity = threadPoolQueueCapacity;
    }

    public String getThreadPoolRejectedPolicy() {
        return threadPoolRejectedPolicy;
    }

    public void setThreadPoolRejectedPolicy(String threadPoolRejectedPolicy) {
        this.threadPoolRejectedPolicy = threadPoolRejectedPolicy;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(this, object);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}