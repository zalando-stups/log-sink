package org.zalando.stups.logsink.service;

import org.springframework.scheduling.annotation.Async;

public class AsyncInstanceLogService extends DelegatingInstanceLogsService {

    public AsyncInstanceLogService(InstanceLogsService delegate) {
        super(delegate);
    }

    @Async
    @Override
    public void handleInstanceLogs(String payload) {
        super.handleInstanceLogs(payload);
    }
}
