package org.zalando.stups.logsink.service;

class DelegatingInstanceLogsService implements InstanceLogsService {

    private final InstanceLogsService delegate;

    DelegatingInstanceLogsService(InstanceLogsService delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handleInstanceLogs(String payload) {
        delegate.handleInstanceLogs(payload);
    }
}
