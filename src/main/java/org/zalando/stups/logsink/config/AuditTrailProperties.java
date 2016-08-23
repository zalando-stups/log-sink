package org.zalando.stups.logsink.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "audittrail")
public class AuditTrailProperties {

    // AUDITTRAIL_URL
    private URI url;
    // AUDITTRAIL_EVENT_NAMESPACE
    private String eventNamespace;
    // AUDITTRAIL_EVENT_VERSION
    private String eventVersion;
    // AUDITTRAIL_EVENT_NAME
    private String eventName;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(final String eventName) {
        this.eventName = eventName;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(final URI url) {
        this.url = url;
    }

    public String getEventNamespace() {
        return eventNamespace;
    }

    public void setEventNamespace(final String eventNamespace) {
        this.eventNamespace = eventNamespace;
    }

    public String getEventVersion() {
        return eventVersion;
    }

    public void setEventVersion(final String eventVersion) {
        this.eventVersion = eventVersion;
    }
}


