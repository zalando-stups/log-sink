package org.zalando.stups.logsink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.stups.logsink.filter.LogSinkZuulFilter;
import org.zalando.stups.logsink.provider.TokenProvider;
import org.zalando.stups.tokens.AccessTokensBean;

@Configuration
public class FilterConfiguration {

    @Autowired
    private AccessTokensBean accessTokensBean;

    @Bean
    public LogSinkZuulFilter getLogSinkZuulFilter() {
        return new LogSinkZuulFilter();
    }

    @Bean
    public TokenProvider getTokenProvider() {
        return new TokenProvider(accessTokensBean);
    }

}
