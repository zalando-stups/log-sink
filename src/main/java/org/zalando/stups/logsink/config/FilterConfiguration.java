package org.zalando.stups.logsink.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.stups.logsink.filter.LogSinkZuulFilter;
import org.zalando.stups.logsink.filter.LoggingZuulFilter;
import org.zalando.stups.logsink.filter.RequestInfoMDCFilter;
import org.zalando.stups.logsink.provider.TokenProvider;
import org.zalando.stups.tokens.AccessTokensBean;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
public class FilterConfiguration {

    @Autowired
    private AccessTokensBean accessTokensBean;

    @Bean
    public LogSinkZuulFilter logSinkZuulFilter() {
        return new LogSinkZuulFilter();
    }

    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider(accessTokensBean);
    }

    @Bean
    public LoggingZuulFilter loggingZuulFilter() {
        return new LoggingZuulFilter();
    }

    @Bean
    public FilterRegistrationBean requestInfoMDCFilter() {
        final FilterRegistrationBean filterRegistration = new FilterRegistrationBean(new RequestInfoMDCFilter());
        filterRegistration.setOrder(HIGHEST_PRECEDENCE);
        return filterRegistration;
    }
}
