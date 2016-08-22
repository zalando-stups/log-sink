package org.zalando.stups.logsink.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestOperations;
import org.zalando.stups.logsink.api.TaupageLogData;
import org.zalando.stups.logsink.config.AuditTrailProperties;
import org.zalando.stups.logsink.rest.client.audittrail.EventType;
import org.zalando.stups.logsink.rest.client.audittrail.TaupageYamlEvent;
import org.zalando.stups.logsink.rest.client.audittrail.TaupageYamlPayload;

import java.net.URI;
import java.net.URISyntaxException;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.RequestEntity.put;

public class AuditTrailLogService {

    private static final int INITIAL_DELAY_MS = 1000;
    private static final int MAX_DELAY = 60000;
    private static final int MULTIPLIER = 5;
    private static final Logger LOG = getLogger(AuditTrailLogService.class);
    private final RestOperations restOperations;
    private final AuditTrailProperties properties;

    public AuditTrailLogService(final RestOperations instanceLogsRestOperations,
                                final AuditTrailProperties properties) {

        this.restOperations = instanceLogsRestOperations;
        this.properties = properties;
    }

    @Async
    @Retryable(backoff = @Backoff(delay = INITIAL_DELAY_MS, maxDelay = MAX_DELAY, multiplier = MULTIPLIER))
    public void sendTaupageYamlEvent(final TaupageLogData logData) throws JsonProcessingException {

        final TaupageYamlEvent event = new TaupageYamlEvent();

        //setting event's header data
        final EventType eventType = new EventType();
        eventType.setName(properties.getEventName());
        eventType.setNameSpace(properties.getEventNamespace());
        eventType.setVersion(properties.getEventVersion());
        event.setEventType(eventType);
        event.setTriggeredAt(logData.getInstanceBootTime());

        //create the logData
        final TaupageYamlPayload payload = new TaupageYamlPayload();
        payload.setAccountId(logData.getAccountId());
        payload.setRegion(logData.getRegion());
        payload.setInstanceId(logData.getInstanceId());
        payload.setTaupageYaml(logData.getPayload());

        //finally, generate EventId/SHA..
        final HashFunction sha256 = Hashing.sha256();
        final ObjectMapper objectMapper = new ObjectMapper();
        final byte[] bytes = objectMapper.writeValueAsBytes(event);
        final HashCode eventId = sha256.hashBytes(bytes);

        final URI auditTrailUrl = properties.getAudittrailUrl();
        final URI dest;
        try {
            dest = new URI(auditTrailUrl.toString() + "/" + eventId);
            LOG.debug("sending taupage YAML Event...");
            restOperations.exchange(put(dest).contentType(APPLICATION_JSON).body(bytes), byte[].class);
        } catch (URISyntaxException e) {
            LOG.error("Unable to construct URI for Audittrail API! BaseURI: {}, Event: {}", auditTrailUrl.toString(),
                      eventId);
        }
    }
}
