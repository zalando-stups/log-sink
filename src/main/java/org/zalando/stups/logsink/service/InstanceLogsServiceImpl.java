package org.zalando.stups.logsink.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.zalando.stups.logsink.config.InstanceLogsProperties;

import java.net.URI;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.RequestEntity.post;

public class InstanceLogsServiceImpl implements InstanceLogsService {

    private final Logger log = getLogger(getClass());

    private final RestOperations rest;
    private final InstanceLogsProperties properties;

    public InstanceLogsServiceImpl(
            final InstanceLogsProperties properties,
            final RestOperations instanceLogsRestOperations) {
        this.rest = instanceLogsRestOperations;
        this.properties = properties;
    }

    @Override
    public void handleInstanceLogs(final String payload) {
        final URI proxyUrl = properties.getProxyUrl();
        log.debug("forwarding instance-logs to {}", proxyUrl);
        rest.exchange(post(proxyUrl)
                .contentType(APPLICATION_JSON)
                .body(payload), String.class);
    }
}
