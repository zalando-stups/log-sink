package org.zalando.stups.logsink.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.zalando.stups.oauth2.spring.client.StupsOAuth2RestTemplate;
import org.zalando.stups.oauth2.spring.client.StupsTokensAccessTokenProvider;
import org.zalando.stups.tokens.AccessTokens;

@Configuration
@EnableConfigurationProperties(InstanceLogsProperties.class)
public class InstanceLogsConfiguration {

    @Bean
    public RestOperations instanceLogsRestOperations(final AccessTokens accessTokens) {
        final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();


        return new StupsOAuth2RestTemplate(
                new StupsTokensAccessTokenProvider("log-sink", accessTokens),
                requestFactory);
    }
}
