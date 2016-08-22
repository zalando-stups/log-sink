package org.zalando.stups.logsink.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: clohmann
 * Date: 22.08.16
 * Time: 15:41
 */
public class TaupageLogData {

    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty("region")
    private String region;
    @JsonProperty("instance_boot_time")
    private String instanceBootTime;
    @JsonProperty("instance_id")
    private String instanceId;
    @JsonProperty("log_data")
    private String payload;
    @JsonProperty("log_type")
    private String logType;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(final String accountId) {
        this.accountId = accountId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getInstanceBootTime() {
        return instanceBootTime;
    }

    public void setInstanceBootTime(final String instanceBootTime) {
        this.instanceBootTime = instanceBootTime;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(final String instanceId) {
        this.instanceId = instanceId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(final String payload) {
        this.payload = payload;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(final String logType) {
        this.logType = logType;
    }
}
