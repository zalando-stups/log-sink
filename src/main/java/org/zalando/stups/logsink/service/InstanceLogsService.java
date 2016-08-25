package org.zalando.stups.logsink.service;

import org.zalando.stups.logsink.api.TaupageLogData;

public interface InstanceLogsService {

    void handleInstanceLogs(TaupageLogData payload);

}
