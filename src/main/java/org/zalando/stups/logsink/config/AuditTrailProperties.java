package org.zalando.stups.logsink.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "audittrail")
public class AuditTrailProperties {

    // AUDITTRAIL_URL
    private URI audittrailUrl;
    // EVENT_NAMESPACE
    private String eventNamespace;
    // EVENT_VERSION
    private String eventVersion;
    // EVENT_NAME
    private String eventName;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(final String eventName) {
        this.eventName = eventName;
    }

    public URI getAudittrailUrl() {
        return audittrailUrl;
    }

    public void setAudittrailUrl(final URI audittrailUrl) {
        this.audittrailUrl = audittrailUrl;
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


