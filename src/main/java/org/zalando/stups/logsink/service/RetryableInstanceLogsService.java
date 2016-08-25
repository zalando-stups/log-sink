package org.zalando.stups.logsink.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.zalando.stups.logsink.api.TaupageLogData;

public class RetryableInstanceLogsService extends DelegatingInstanceLogsService {

    private static final int INITIAL_DELAY_MS = 1000;
    private static final int MAX_DELAY = 60000;
    private static final int MULTIPLIER = 5;

    public RetryableInstanceLogsService(InstanceLogsService delegate) {
        super(delegate);
    }

    @Retryable(backoff = @Backoff(delay = INITIAL_DELAY_MS, maxDelay = MAX_DELAY, multiplier = MULTIPLIER))
    @Override
    public void handleInstanceLogs(TaupageLogData payload) {
        super.handleInstanceLogs(payload);
    }
}
