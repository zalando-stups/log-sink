package org.zalando.stups.logsink.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.zalando.spring.boot.async.AsyncAutoconfiguration;
import org.zalando.stups.logsink.metrics.AsyncPublicMetrics;

@Configuration
@AutoConfigureAfter(AsyncAutoconfiguration.class)
@ConditionalOnBean(value = ThreadPoolTaskExecutor.class)
public class AsyncMetricsConfiguration {

    @Bean
    public AsyncPublicMetrics asyncPublicMetrics(final ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return new AsyncPublicMetrics("async.executor", threadPoolTaskExecutor);
    }

}
