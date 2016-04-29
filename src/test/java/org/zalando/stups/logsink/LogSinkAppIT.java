package org.zalando.stups.logsink;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

@SpringApplicationConfiguration(LogSinkApp.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("it")
public class LogSinkAppIT {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    private static final String CORRECT_USER = "it-user";
    private static final String CORRECT_PASSWORD = "t0p5ecr3t";

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(Integer.valueOf(System.getProperty("wiremock.port", "10080")));

    @Value("${local.server.port}")
    private int port;


    @Test
    public void testPushLogs() throws Exception {
        stubFor(post(urlPathEqualTo("/api/instance-logs")).willReturn(aResponse().withStatus(200)));

        final TestRestTemplate restOperations = new TestRestTemplate(CORRECT_USER, CORRECT_PASSWORD);
        final ResponseEntity<String> response = restOperations.exchange(
                RequestEntity.post(URI.create("http://localhost:" + port + "/api/instance-logs")).build(),
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }
}
