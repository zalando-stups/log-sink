package org.zalando.stups.logsink.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;
import org.zalando.stups.logsink.service.AsyncInstanceLogService;
import org.zalando.stups.logsink.service.InstanceLogsServiceImpl;
import org.zalando.stups.logsink.service.RetryableInstanceLogsService;
import org.zalando.stups.oauth2.spring.client.StupsOAuth2RestTemplate;
import org.zalando.stups.oauth2.spring.client.StupsTokensAccessTokenProvider;
import org.zalando.stups.tokens.AccessTokens;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

@Configuration
@EnableConfigurationProperties(InstanceLogsProperties.class)
public class InstanceLogsConfiguration {

    @Bean
    @Primary
    public AsyncInstanceLogService asyncInstanceLogService(
            final RetryableInstanceLogsService retryableInstanceLogsService) {
        return new AsyncInstanceLogService(retryableInstanceLogsService);
    }

    @Bean
    public RetryableInstanceLogsService retryableInstanceLogsService(
            final InstanceLogsServiceImpl instanceLogsServiceImpl) {
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
    public ClientHttpRequestFactory instanceLogsRequestFactory(final Logbook logbook) {
        CloseableHttpClient client = HttpClientBuilder.create()
                .addInterceptorFirst(new LogbookHttpRequestInterceptor(logbook))
                .addInterceptorFirst(new LogbookHttpResponseInterceptor())
                .build();
        final HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setHttpClient(client);
        return httpRequestFactory;
    }
}
