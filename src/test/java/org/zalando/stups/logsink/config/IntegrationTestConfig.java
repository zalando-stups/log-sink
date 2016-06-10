package org.zalando.stups.logsink.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
@AutoConfigureBefore(InstanceLogsConfiguration.class)
public class IntegrationTestConfig {

    @Bean
    public ClientHttpRequestFactory instanceLogsRequestFactory() {
        // Replacing Apache's HttpClient with Spring's simple client in this test, because the Apache one caches
        // connections, tries to reuse the over multiple tests and finally fails becuase WireMock will be reset
        // in each test.
        return new SimpleClientHttpRequestFactory();
    }
}
