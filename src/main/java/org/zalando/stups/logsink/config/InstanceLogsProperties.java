package org.zalando.stups.logsink.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "instance-logs")
public class InstanceLogsProperties {

    private URI proxyUrl;

    public URI getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(URI proxyUrl) {
        this.proxyUrl = proxyUrl;
    }
}


