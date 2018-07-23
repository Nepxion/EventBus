package com.nepxion.eventbus.thread.entity;

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

import com.nepxion.eventbus.thread.constant.ThreadConstant;

public class ThreadCustomization implements Serializable {
    private static final long serialVersionUID = -927120657442228832L;

    private boolean threadPoolMultiMode = false;
    private String threadPoolSharedName = ThreadConstant.DEFAULT_THREADPOOL_SHARED_NAME;
    private boolean threadPoolNameCustomized = true;
    private boolean threadPoolNameIPShown = true;

    public boolean isThreadPoolMultiMode() {
        return threadPoolMultiMode;
    }

    public void setThreadPoolMultiMode(boolean threadPoolMultiMode) {
        this.threadPoolMultiMode = threadPoolMultiMode;
    }

    public String getThreadPoolSharedName() {
        return threadPoolSharedName;
    }

    public void setThreadPoolSharedName(String threadPoolSharedName) {
        this.threadPoolSharedName = threadPoolSharedName;
    }

    public boolean isThreadPoolNameCustomized() {
        return threadPoolNameCustomized;
    }

    public void setThreadPoolNameCustomized(boolean threadPoolNameCustomized) {
        this.threadPoolNameCustomized = threadPoolNameCustomized;
    }

    public boolean isThreadPoolNameIPShown() {
        return threadPoolNameIPShown;
    }

    public void setThreadPoolNameIPShown(boolean threadPoolNameIPShown) {
        this.threadPoolNameIPShown = threadPoolNameIPShown;
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