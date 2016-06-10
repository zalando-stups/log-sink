package org.zalando.stups.logsink.config;


import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.zalando.stups.logsink.service.AsyncInstanceLogService;
import org.zalando.stups.logsink.service.InstanceLogsService;
import org.zalando.stups.tokens.AccessTokens;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ContextConfiguration
public class InstanceLogsConfigurationTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired(required = false)
    private InstanceLogsService instanceLogsService;

    @Test
    public void instanceLogServiceBeanIsPresent() throws Exception {
        assertThat(instanceLogsService)
                .isNotNull()
                .isInstanceOf(AsyncInstanceLogService.class);

    }

    @Configuration
    @Import(InstanceLogsConfiguration.class)
    static class TestConfig {

        @Bean
        AccessTokens accessTokens() {
            return mock(AccessTokens.class);
        }

    }
}
