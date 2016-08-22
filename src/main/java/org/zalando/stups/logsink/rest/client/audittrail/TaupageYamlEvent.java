package org.zalando.stups.logsink.rest.client.audittrail;

/**
 * Author: clohmann
 * Date: 22.08.16
 * Time: 16:14
 */
public class TaupageYamlEvent {

    private EventType eventType;

    private String triggeredAt;

    private TaupageYamlPayload payload;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(final EventType eventType) {
        this.eventType = eventType;
    }

    public TaupageYamlPayload getPayload() {
        return payload;
    }

    public void setPayload(final TaupageYamlPayload payload) {
        this.payload = payload;
    }

    public String getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(final String triggeredAt) {
        this.triggeredAt = triggeredAt;
    }
}
