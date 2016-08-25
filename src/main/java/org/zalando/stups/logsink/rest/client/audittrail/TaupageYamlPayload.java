package org.zalando.stups.logsink.rest.client.audittrail;

import java.util.Map;

/**
 * Author: clohmann
 * Date: 22.08.16
 * Time: 16:20
 */
public class TaupageYamlPayload {

    private String accountId;

    private String region;

    private String instanceId;

    private Map taupageYaml;

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

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(final String instanceId) {
        this.instanceId = instanceId;
    }

    public Map getTaupageYaml() {
        return taupageYaml;
    }

    public void setTaupageYaml(final Map taupageYaml) {
        this.taupageYaml = taupageYaml;
    }
}
