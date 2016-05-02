package org.zalando.stups.logsink;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.env.Environment;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringApplicationConfiguration(LogSinkApp.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("it")
public class LogSinkAppIT {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    private static final String CORRECT_USER = "it-user";
    private static final String CORRECT_PASSWORD = "t0p5ecr3t";

    private final Logger log = getLogger(getClass());

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(Integer.valueOf(System.getProperty("wiremock.port", "10080")));

    @Value("${local.server.port}")
    private int port;

    @Value("${local.management.port}")
    private int managementPort;

    private String jsonPayload;
    private ImmutableMap<String, Object> payload;

    @Autowired
    Environment env;

    @Before
    public void setUp() throws Exception {
        payload = ImmutableMap.of(
                "foo", "bar",
                "an_integer", 5,
                "complex_inner_type", singletonMap("hello", "World"));
        jsonPayload = new ObjectMapper().writeValueAsString(payload);
    }

    @Test
    public void testManagementEndpoints() throws Exception {
        final TestRestTemplate restOperations = new TestRestTemplate();
        log.info("ENVIRONMENT:\n{}", restOperations.getForObject("http://localhost:" + managementPort + "/env", String.class));
        log.info("CONFIG_PROPS:\n{}", restOperations.getForObject("http://localhost:" + managementPort + "/configprops", String.class));
        log.info("ROUTES:\n{}", restOperations.getForObject("http://localhost:" + managementPort + "/routes", String.class));
    }

    @Test
    public void testPushLogs() throws Exception {
        final TestRestTemplate restOperations = new TestRestTemplate(CORRECT_USER, CORRECT_PASSWORD);

        stubFor(post(urlPathEqualTo("/api/instance-logs"))
                .withRequestBody(equalToJson(jsonPayload))
                .withHeader(AUTHORIZATION, equalTo("Bearer 1234567890")) // static test token, see config/application-it.yml
                .willReturn(aResponse().withStatus(200)));

        final URI url = URI.create("http://localhost:" + port + "/instance-logs");
        final ResponseEntity<String> response = restOperations.exchange(RequestEntity.post(url).contentType(APPLICATION_JSON).body(payload), String.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }
}
