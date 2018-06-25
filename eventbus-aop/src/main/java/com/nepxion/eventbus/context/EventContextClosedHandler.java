package com.nepxion.eventbus.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import com.nepxion.eventbus.thread.ThreadPoolFactory;

/*
通过ContextClosedEvent触发线程池关闭，否则会引起如下错误。原因是Spring销毁的时候，先销毁ThreadPoolExecutor的BlockQueue，再执行线程池shutdown，于是发生memory leak
The web application [ROOT] appears to have started a thread named [EventBus-192.168.0.107-thread-0] but has failed to stop it. This is very likely to create a memory leak. Stack trace of thread:
    sun.misc.Unsafe.park(Native Method)
    java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
    java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
    java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
    java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
    java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
    java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
    java.lang.Thread.run(Thread.java:745)
*/
public class EventContextClosedHandler implements ApplicationListener<ContextClosedEvent> {
    @Autowired
    ThreadPoolFactory threadPoolFactory;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        threadPoolFactory.shutdown();
    }
}