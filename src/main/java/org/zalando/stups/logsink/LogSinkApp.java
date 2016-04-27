package org.zalando.stups.logsink;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringCloudApplication
@EnableZuulProxy
public class LogSinkApp {

    public static void main(final String[] args) {
        SpringApplication.run(LogSinkApp.class, args);
    }
}
