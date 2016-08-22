package org.zalando.stups.logsink.service;

import org.springframework.scheduling.annotation.Async;
import org.zalando.stups.logsink.api.TaupageLogData;

public class AsyncInstanceLogService extends DelegatingInstanceLogsService {

    public AsyncInstanceLogService(final InstanceLogsService delegate) {
        super(delegate);
    }

    @Async
    @Override
    public void handleInstanceLogs(final TaupageLogData payload) {
        super.handleInstanceLogs(payload);
    }
}
