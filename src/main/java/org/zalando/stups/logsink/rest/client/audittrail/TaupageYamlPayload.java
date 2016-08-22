package org.zalando.stups.logsink.rest.client.audittrail;

/**
 * Author: clohmann
 * Date: 22.08.16
 * Time: 16:20
 */
public class TaupageYamlPayload {

    private String accountId;

    private String region;

    private String instanceId;

    private String taupageYaml;

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

    public String getTaupageYaml() {
        return taupageYaml;
    }

    public void setTaupageYaml(final String taupageYaml) {
        this.taupageYaml = taupageYaml;
    }
}
