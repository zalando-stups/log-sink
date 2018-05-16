package org.zalando.stups.logsink;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.zalando.stups.logsink.config.AuditTrailProperties;
import org.zalando.stups.logsink.rest.client.audittrail.EventType;
import org.zalando.stups.logsink.rest.client.audittrail.TaupageYamlEvent;
import org.zalando.stups.logsink.rest.client.audittrail.TaupageYamlPayload;

import java.net.URI;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LogSinkApp.class)
@ActiveProfiles("it")
@SuppressWarnings("unchecked")
public class LogSinkAppIT {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    private static final String CORRECT_USER = "it-user";
    private static final String CORRECT_PASSWORD = "t0p5ecr3t";

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(
            Integer.valueOf(System.getProperty("wiremock.port", "10080")));

    @Rule
    public OutputCapture capture = new OutputCapture();

    private final Logger log = getLogger(getClass());

    @Value("${local.server.port}")
    private int port;

    @Value("${local.management.port}")
    private int managementPort;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuditTrailProperties auditTrailProperties;

    private String intanceLogsJsonPayload;
    private Map instanceLogsPayload;
    private TaupageYamlEvent taupageYamlEvent;
    private Map userDataPayload;
    private byte[] auditTrailJsonPayload;
    private String eventID;

    @Before
    public void setUp() throws Exception {
        userDataPayload = ImmutableMap.of("runtime", "Docker",
                                          "mint_bucket", "some-mint-bucket",
                                          "ports", singletonMap("8080", "8080"),
                                          "environment", singletonMap("DB_USER", "aDbUser"));
        final byte[] json = objectMapper.writeValueAsBytes(userDataPayload);
        final byte[] base64Dncoded = Base64.getMimeEncoder().encode(json);

        instanceLogsPayload = newHashMap();
        instanceLogsPayload.put("account_id", "12345657890");
        instanceLogsPayload.put("region", "eu-central-1");
        instanceLogsPayload.put("instance_boot_time", "2016-08-13T10:45:03.000Z");
        instanceLogsPayload.put("instance_id", "i-239087465fe035");
        instanceLogsPayload.put("log_data", new String(base64Dncoded));
        instanceLogsPayload.put("log_type", "USER_DATA");

        taupageYamlEvent = new TaupageYamlEvent();
        taupageYamlEvent.setTriggeredAt((String) instanceLogsPayload.get("instance_boot_time"));
        EventType eventType = new EventType();
        eventType.setName(auditTrailProperties.getEventName());
        eventType.setNamespace(auditTrailProperties.getEventNamespace());
        eventType.setVersion(auditTrailProperties.getEventVersion());
        taupageYamlEvent.setEventType(eventType);
        TaupageYamlPayload taupageYamlPayload = new TaupageYamlPayload();
        taupageYamlPayload.setAccountId((String) instanceLogsPayload.get("account_id"));
        taupageYamlPayload.setInstanceId((String) instanceLogsPayload.get("instance_id"));
        taupageYamlPayload.setRegion((String) instanceLogsPayload.get("region"));
        taupageYamlPayload.setTaupageYaml(userDataPayload);
        taupageYamlEvent.setPayload(taupageYamlPayload);

        auditTrailJsonPayload = objectMapper.writeValueAsBytes(taupageYamlEvent);
        final HashFunction sha256 = Hashing.sha256();
        eventID = sha256.hashBytes(auditTrailJsonPayload).toString();
        intanceLogsJsonPayload = objectMapper.writeValueAsString(instanceLogsPayload);
    }

    @Test
    public void testManagementEndpoints() throws Exception {
        final TestRestTemplate restOperations = new TestRestTemplate();
        log.info("ENVIRONMENT:\n{}",
                 restOperations.getForObject("http://localhost:" + managementPort + "/env", String.class));
        log.info("CONFIG_PROPS:\n{}",
                 restOperations.getForObject("http://localhost:" + managementPort + "/configprops", String.class));
        log.info("AUTOCONFIG:\n{}",
                 restOperations.getForObject("http://localhost:" + managementPort + "/autoconfig", String.class));
    }

    @Test
    public void testPushTaupageLog() throws Exception {
        final TestRestTemplate restOperations = new TestRestTemplate(CORRECT_USER, CORRECT_PASSWORD);

        stubFor(post(urlPathEqualTo("/api/instance-logs")).willReturn(aResponse().withStatus(201)));
        stubFor(put(urlPathMatching("/events/.*")).willReturn(aResponse().withStatus(200)));

        final URI url = URI.create("http://localhost:" + port + "/instance-logs");
        final ResponseEntity<String> response = restOperations.exchange(
                RequestEntity.post(url).contentType(APPLICATION_JSON).body(
                        instanceLogsPayload), String.class);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        log.debug("Waiting for async tasks to finish");
        TimeUnit.SECONDS.sleep(1);

        verify(postRequestedFor(urlPathEqualTo("/api/instance-logs"))
                       .withRequestBody(equalToJson(intanceLogsJsonPayload))
                       .withHeader(AUTHORIZATION, equalTo("Bearer 1234567890")));

        verify(putRequestedFor(urlPathEqualTo("/events/" + eventID))
                       .withRequestBody(equalToJson(new String(auditTrailJsonPayload)))
                       .withHeader(AUTHORIZATION, equalTo("Bearer 1234567890")));

        log.info("METRICS:\n{}",
                 restOperations.getForObject("http://localhost:" + managementPort + "/metrics", String.class));
    }

    @Test
    public void testPushTaupageLogWithRetry() throws Exception {
        final TestRestTemplate restOperations = new TestRestTemplate(CORRECT_USER, CORRECT_PASSWORD);

        stubFor(post(urlPathEqualTo("/api/instance-logs")).willReturn(aResponse().withStatus(201).withFixedDelay(100)));
        stubFor(put(urlEqualTo("/events/" + eventID)).willReturn(aResponse().withStatus(429).withFixedDelay(100)));

        final URI url = URI.create("http://localhost:" + port + "/instance-logs");
        final ResponseEntity<String> response = restOperations.exchange(
                RequestEntity.post(url).contentType(APPLICATION_JSON).body(
                        instanceLogsPayload), String.class);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        log.debug("Waiting for async tasks to finish");
        TimeUnit.MILLISECONDS.sleep(7500);

        verify(postRequestedFor(urlPathEqualTo("/api/instance-logs"))
                       .withRequestBody(equalToJson(intanceLogsJsonPayload))
                       .withHeader(AUTHORIZATION, equalTo("Bearer 1234567890")));

        verify(3, putRequestedFor(urlPathEqualTo("/events/" + eventID))
                .withRequestBody(equalToJson(new String(auditTrailJsonPayload)))
                .withHeader(AUTHORIZATION, equalTo("Bearer 1234567890")));
    }

    @Test
    public void testPushStandardLog() throws Exception {
        final TestRestTemplate restOperations = new TestRestTemplate(CORRECT_USER, CORRECT_PASSWORD);

        stubFor(post(urlPathEqualTo("/api/instance-logs")).willReturn(aResponse().withStatus(201)));

        //change the log type to something different than 'USER_DATA' to avoid sending an audit trail event
        instanceLogsPayload.put("log_type", "SOMETHING_DIFFERENT");

        final URI url = URI.create("http://localhost:" + port + "/instance-logs");
        final ResponseEntity<String> response = restOperations.exchange(
                RequestEntity.post(url).contentType(APPLICATION_JSON).body(
                        instanceLogsPayload), String.class);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        log.debug("Waiting for async tasks to finish");
        TimeUnit.SECONDS.sleep(1);

        final String standardLogsPayload = objectMapper.writeValueAsString(instanceLogsPayload);
        verify(postRequestedFor(urlPathEqualTo("/api/instance-logs"))
                       .withRequestBody(equalToJson(standardLogsPayload))
                       .withHeader(AUTHORIZATION, equalTo("Bearer 1234567890")));

        verify(0, putRequestedFor(urlPathMatching("/events/.*")));
    }

    @Test
    public void testPushLogsUnauthenticated() throws Exception {
        final TestRestTemplate restOperations = new TestRestTemplate();

        stubFor(post(urlPathEqualTo("/api/instance-logs")).willReturn(aResponse().withStatus(201)));

        final URI url = URI.create("http://localhost:" + port + "/instance-logs");
        final ResponseEntity<String> response = restOperations.exchange(
                RequestEntity.post(url).contentType(APPLICATION_JSON).body(
                        instanceLogsPayload), String.class);
        assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
    }

    @Test
    public void testPushLogsWrongPassword() throws Exception {
        final TestRestTemplate restOperations = new TestRestTemplate(CORRECT_USER, "wrong-password");

        stubFor(post(urlPathEqualTo("/api/instance-logs")).willReturn(aResponse().withStatus(201)));

        final URI url = URI.create("http://localhost:" + port + "/instance-logs");
        final ResponseEntity<String> response = restOperations.exchange(
                RequestEntity.post(url).contentType(APPLICATION_JSON).body(
                        instanceLogsPayload), String.class);
        assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
    }

    @Test
    public void testPushLogsUpstreamFailsOnce() throws Exception {
        final TestRestTemplate restOperations = new TestRestTemplate(CORRECT_USER, CORRECT_PASSWORD);

        // first calls fails
        stubFor(post(urlPathEqualTo("/api/instance-logs")).inScenario("Test Retry")
                        .whenScenarioStateIs(STARTED)
                        .willReturn(aResponse().withStatus(500))
                        .willSetStateTo("retry"));

        // second one succeeds
        stubFor(post(urlPathEqualTo("/api/instance-logs")).inScenario("Test Retry")
                        .whenScenarioStateIs("retry")
                        .willReturn(aResponse().withStatus(201)));

        stubFor(put(urlPathMatching("/events/.*")).willReturn(aResponse().withStatus(200)));

        final URI url = URI.create("http://localhost:" + port + "/instance-logs");
        final ResponseEntity<String> response = restOperations.exchange(
                RequestEntity.post(url).contentType(APPLICATION_JSON).body(
                        instanceLogsPayload), String.class);

        // even if upstream request fails, due to async processing this endpoint should return a success message
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        log.debug("Waiting for async tasks to finish");
        TimeUnit.SECONDS.sleep(2);

        // endpoint should have been called twice
        verify(2, postRequestedFor(urlPathEqualTo("/api/instance-logs"))
                .withRequestBody(equalToJson(intanceLogsJsonPayload))
                .withHeader(AUTHORIZATION, equalTo("Bearer 1234567890")));
    }

    @Test
    public void testInvokeRootEndpoint() throws Exception {
        final TestRestTemplate restOperations = new TestRestTemplate();

        final URI url = URI.create("http://localhost:" + port);
        final ResponseEntity<String> response = restOperations.exchange(RequestEntity.get(url).build(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
    }

    @Test
    public void testAuditLogDataIsObfuscated() throws Exception {
        final TestRestTemplate restOperations = new TestRestTemplate(CORRECT_USER, CORRECT_PASSWORD);

        stubFor(post(urlPathEqualTo("/api/instance-logs")).willReturn(aResponse().withStatus(201)));

        instanceLogsPayload.put("log_type", "AUDIT_LOG");

        final URI url = URI.create("http://localhost:" + port + "/instance-logs");
        final ResponseEntity<String> response = restOperations.exchange(
                RequestEntity.post(url).contentType(APPLICATION_JSON).body(
                        instanceLogsPayload), String.class);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        log.debug("Waiting for async tasks to finish");
        TimeUnit.SECONDS.sleep(1);

        assertThat(capture.toString()).contains("\"log_data\":\"***\"");
    }
}
