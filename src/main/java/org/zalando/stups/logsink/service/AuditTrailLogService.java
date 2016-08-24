package org.zalando.stups.logsink.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestOperations;
import org.zalando.stups.logsink.api.TaupageLogData;
import org.zalando.stups.logsink.config.AuditTrailProperties;
import org.zalando.stups.logsink.rest.client.audittrail.EventType;
import org.zalando.stups.logsink.rest.client.audittrail.TaupageYamlEvent;
import org.zalando.stups.logsink.rest.client.audittrail.TaupageYamlPayload;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.RequestEntity.put;

public class AuditTrailLogService {

    private static final Logger LOG = getLogger(AuditTrailLogService.class);

    private static final int INITIAL_DELAY_MS = 1000;
    private static final int MAX_DELAY = 60000;
    private static final int MULTIPLIER = 5;

    private final Base64.Decoder base64Decoder = Base64.getMimeDecoder();

    private final YAMLMapper yamlMapper;
    private final ObjectMapper objectMapper;
    private final RestOperations restOperations;
    private final AuditTrailProperties properties;

    @Autowired
    public AuditTrailLogService(final RestOperations instanceLogsRestOperations,
                                final AuditTrailProperties properties, ObjectMapper objectMapper,
                                final YAMLMapper yamlMapper) {

        this.restOperations = instanceLogsRestOperations;
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.yamlMapper = yamlMapper;
    }

    @Async
    @Retryable(backoff = @Backoff(delay = INITIAL_DELAY_MS, maxDelay = MAX_DELAY, multiplier = MULTIPLIER))
    public void sendTaupageYamlEvent(final TaupageLogData logData) throws JsonProcessingException {

        final Optional<Map> userData = getUserData(logData);

        //setting up an event
        final TaupageYamlEvent event = new TaupageYamlEvent();
        final EventType eventType = new EventType();
        eventType.setName(properties.getEventName());
        eventType.setNamespace(properties.getEventNamespace());
        eventType.setVersion(properties.getEventVersion());
        event.setEventType(eventType);
        event.setTriggeredAt(logData.getInstanceBootTime());

        //create the logData
        final TaupageYamlPayload payload = new TaupageYamlPayload();
        payload.setAccountId(logData.getAccountId());
        payload.setRegion(logData.getRegion());
        payload.setInstanceId(logData.getInstanceId());
        payload.setTaupageYaml(userData.orElse(ImmutableMap.of()));
        event.setPayload(payload);

        // marshal event to json
        final byte[] marshaled = objectMapper.writeValueAsBytes(event);

        //finally, generate EventId/SHA..
        final String eventId = createEventID(marshaled);

        final URI auditTrailUrl = properties.getUrl();
        final URI dest;
        try {
            dest = new URI(auditTrailUrl.toString() + "/" + eventId);
            LOG.debug("sending taupage YAML Event...");
            restOperations.exchange(put(dest).contentType(APPLICATION_JSON).body(marshaled), byte[].class);
        } catch (URISyntaxException e) {
            LOG.error("Unable to construct URI for Audittrail API! BaseURI: {}, Event: {}", auditTrailUrl.toString(),
                      eventId);
        }
    }

    private Optional<Map> getUserData(final TaupageLogData logData) {
        //first decode it and parse a map from user data
        final String encodedUserData = logData.getPayload();
        return Optional.ofNullable(encodedUserData)
                .map(base64Decoder::decode)
                .map(data -> {
                    try {
                        return yamlMapper.readValue(data, Map.class);
                    } catch (IOException e) {
                        LOG.error("could not read User Data YAML of instance: {}, account: {}", logData.getInstanceId(),
                                  logData.getAccountId());
                    }
                    return ImmutableMap.of();
                });
    }

    private String createEventID(final byte[] marshaled) throws JsonProcessingException {
        final HashFunction sha256 = Hashing.sha256();
        return sha256.hashBytes(marshaled).toString();
    }
}
