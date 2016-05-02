package org.zalando.stups.logsink;

import org.junit.Test;

/**
 * This shows how to run the application with a minimal configuration.
 */
public class LogSinkAppMainIT {

    @Test
    public void testInvokeMainMethod() throws Exception {
        LogSinkApp.main(new String[]{
                "--tokens.test-tokens=log-sink=1234567890",
                "--tokens.access-token-uri=http://localhost:10081",
                "--tokens.credentials-directory=.",
                "--zuul.routes.instance-logs.url=http://localhost:10080/api/instance-logs"});
    }
}
