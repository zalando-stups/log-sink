package org.zalando.stups.logsink.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.stups.logsink.service.AuditTrailLogService;
import org.zalando.stups.logsink.service.InstanceLogsService;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class InstanceLogsController {

    private static final Logger LOG = LoggerFactory.getLogger(InstanceLogsController.class);

    private final InstanceLogsService instanceLogsService;
    private final AuditTrailLogService auditTrailLogService;

    @Autowired
    public InstanceLogsController(final InstanceLogsService instanceLogsService,
                                  final AuditTrailLogService auditTrailLogService) {
        this.instanceLogsService = instanceLogsService;
        this.auditTrailLogService = auditTrailLogService;
    }

    @ResponseStatus(CREATED)
    @RequestMapping(method = POST, path = "/instance-logs", consumes = APPLICATION_JSON_VALUE)
    public void receiveInstanceLogs(@RequestBody final TaupageLogData data) throws IOException {
//        instanceLogsService.handleInstanceLogs(data);
        if ("USER_DATA".equals(data.getLogType())) {
            LOG.debug("Instance-log is a taupage-yaml");
            auditTrailLogService.sendTaupageYamlEvent(data);
        }
    }
}
