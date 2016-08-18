package org.zalando.stups.logsink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = @Filter(Configuration.class))
@EnableCircuitBreaker
@EnableRetry(proxyTargetClass = true)
public class LogSinkApp {

    public static void main(final String[] args) {
        SpringApplication.run(LogSinkApp.class, args);
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.setProtocol("org.apache.coyote.http11.Http11Nio2Protocol");
        return tomcat;
    }
}
