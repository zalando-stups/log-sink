package org.zalando.stups.logsink.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.BodyObfuscator;
import org.zalando.logbook.spring.LogbookAutoConfiguration;

import java.util.regex.Pattern;

@Configuration
@AutoConfigureBefore(LogbookAutoConfiguration.class)
public class LogbookConfig {

    private static final Pattern LOG_TYPE_AUDIT_LOG = Pattern.compile("\"log_type\":\\s*\"AUDIT_LOG\"");
    private static final Pattern LOG_DATA = Pattern.compile("\"log_data\":\\s*\".*?\"");
    private static final String LOG_DATA_OBFUSCATED = "\"log_data\":\"***\"";

    @Bean
    public BodyObfuscator auditLogBodyObfuscator() {
        return (contentType, body) -> {
            if (LOG_TYPE_AUDIT_LOG.matcher(body).find()) {
                return LOG_DATA.matcher(body).replaceFirst(LOG_DATA_OBFUSCATED);
            } else {
                return body;
            }
        };
    }
}
