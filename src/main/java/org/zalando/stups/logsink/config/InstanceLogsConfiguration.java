package org.zalando.stups.logsink.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.zalando.stups.logsink.service.AsyncInstanceLogService;
import org.zalando.stups.logsink.service.InstanceLogsServiceImpl;
import org.zalando.stups.logsink.service.RetryableInstanceLogsService;
import org.zalando.stups.oauth2.spring.client.StupsOAuth2RestTemplate;
import org.zalando.stups.oauth2.spring.client.StupsTokensAccessTokenProvider;
import org.zalando.stups.tokens.AccessTokens;

@Configuration
@EnableConfigurationProperties(InstanceLogsProperties.class)
public class InstanceLogsConfiguration {

    @Bean
    @Primary
    public AsyncInstanceLogService asyncInstanceLogService(final RetryableInstanceLogsService retryableInstanceLogsService) {
        return new AsyncInstanceLogService(retryableInstanceLogsService);
    }

    @Bean
    public RetryableInstanceLogsService retryableInstanceLogsService(final InstanceLogsServiceImpl instanceLogsServiceImpl) {
        return new RetryableInstanceLogsService(instanceLogsServiceImpl);
    }

    @Bean
    public InstanceLogsServiceImpl instanceLogsServiceImpl(
            final InstanceLogsProperties instanceLogsProperties,
            final RestOperations instanceLogsRestOperations) {
        return new InstanceLogsServiceImpl(instanceLogsProperties, instanceLogsRestOperations);
    }

    @Bean
    public RestOperations instanceLogsRestOperations(
            final AccessTokens accessTokens,
            final ClientHttpRequestFactory instanceLogsRequestFactory) {
        return new StupsOAuth2RestTemplate(
                new StupsTokensAccessTokenProvider("log-sink", accessTokens),
                instanceLogsRequestFactory);
    }

    @Bean
    @ConditionalOnMissingBean(value = ClientHttpRequestFactory.class, name = "instanceLogsRequestFactory")
    public ClientHttpRequestFactory instanceLogsRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();
    }
}
