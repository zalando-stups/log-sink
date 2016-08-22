package org.zalando.stups.logsink.rest.client.audittrail;

/**
 * Author: clohmann
 * Date: 22.08.16
 * Time: 16:19
 */
public class EventType {

    private String nameSpace;
    private String name;
    private String version;

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(final String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }
}
