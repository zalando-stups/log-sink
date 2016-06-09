package org.zalando.stups.logsink.metrics;

import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class AsyncPublicMetrics implements PublicMetrics {

    private static final String THREAD_POOL = ".threadPool";
    private static final String QUEUE = ".queue";

    private final String prefix;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public AsyncPublicMetrics(String prefix, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        Assert.hasText(prefix, "Prefix must not be empty");
        Assert.notNull(threadPoolTaskExecutor, "ThreadPoolTaskExecutor must not be null");
        this.prefix = prefix;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @Override
    public Collection<Metric<?>> metrics() {
        final ThreadPoolExecutor executor = this.threadPoolTaskExecutor.getThreadPoolExecutor();
        final BlockingQueue<Runnable> queue = executor.getQueue();
        return asList(
                new Metric<>(prefix + THREAD_POOL + ".poolSize", executor.getPoolSize()),
                new Metric<>(prefix + THREAD_POOL + ".corePoolSize", executor.getCorePoolSize()),
                new Metric<>(prefix + THREAD_POOL + ".largestPoolSize", executor.getLargestPoolSize()),
                new Metric<>(prefix + THREAD_POOL + ".maximumPoolSize", executor.getMaximumPoolSize()),
                new Metric<>(prefix + THREAD_POOL + ".activeCount", executor.getActiveCount()),
                new Metric<>(prefix + THREAD_POOL + ".completedTaskCount", executor.getCompletedTaskCount()),
                new Metric<>(prefix + THREAD_POOL + ".taskPoolSize", executor.getTaskCount()),
                new Metric<>(prefix + THREAD_POOL + ".keepAliveTimeNanoSeconds", executor.getKeepAliveTime(NANOSECONDS)),
                new Metric<>(prefix + THREAD_POOL + QUEUE + ".size", queue.size()),
                new Metric<>(prefix + THREAD_POOL + QUEUE + ".remainingCapacity", queue.remainingCapacity())
        );
    }
}
