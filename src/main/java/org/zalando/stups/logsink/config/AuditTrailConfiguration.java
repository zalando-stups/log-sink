package org.zalando.stups.logsink.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.zalando.stups.logsink.service.AuditTrailLogService;

/**
 * Author: clohmann
 * Date: 22.08.16
 * Time: 15:05
 */
@Configuration
@EnableConfigurationProperties(AuditTrailProperties.class)
public class AuditTrailConfiguration {

    @Bean
    public AuditTrailLogService auditTrailLogService(final AuditTrailProperties auditTrailProperties,
                                                     final RestOperations instanceLogsRestOperations) {
        return new AuditTrailLogService(instanceLogsRestOperations, auditTrailProperties);
    }

}
