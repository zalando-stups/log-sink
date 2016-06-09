package org.zalando.stups.logsink.service;

import org.springframework.scheduling.annotation.Async;

public interface InstanceLogsService {

    @Async
    void handleInstanceLogs(String payload);

}
