package org.zalando.stups.logsink.service;

import org.zalando.stups.logsink.api.TaupageLogData;

class DelegatingInstanceLogsService implements InstanceLogsService {

    private final InstanceLogsService delegate;

    DelegatingInstanceLogsService(InstanceLogsService delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handleInstanceLogs(TaupageLogData payload) {
        delegate.handleInstanceLogs(payload);
    }
}
