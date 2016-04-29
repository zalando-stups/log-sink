package org.zalando.stups.logsink;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

@SpringApplicationConfiguration(LogSinkApp.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("it")
public class LogSinkAppIT {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private final TestRestTemplate restOperations = new TestRestTemplate();

    @Test
    public void testPushLogs() throws Exception {


    }
}
