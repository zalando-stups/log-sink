package org.zalando.stups.logsink.config;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.stups.logsink.filter.ClientErrorLogFilter;
import org.zalando.stups.logsink.filter.RequestInfoMDCFilter;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean requestInfoMDCFilter() {
        final FilterRegistrationBean filterRegistration = new FilterRegistrationBean(new RequestInfoMDCFilter());
        filterRegistration.setOrder(HIGHEST_PRECEDENCE);
        return filterRegistration;
    }

    @Bean
    public FilterRegistrationBean clientErrorLogFilter() {
        final FilterRegistrationBean filterRegistration = new FilterRegistrationBean(new ClientErrorLogFilter());
        filterRegistration.setOrder(HIGHEST_PRECEDENCE + 1);
        return filterRegistration;
    }
}
