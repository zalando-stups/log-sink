package org.zalando.stups.logsink.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
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
    public YAMLMapper yamlMapper(Jackson2ObjectMapperBuilder builder) {
        YAMLMapper yamlMapper = new YAMLMapper();
        builder.configure(yamlMapper);
        return yamlMapper;
    }

    @Bean
    public AuditTrailLogService auditTrailLogService(final AuditTrailProperties auditTrailProperties,
                                                     final RestOperations instanceLogsRestOperations,
                                                     final ObjectMapper objectMapper, final YAMLMapper yamlMapper) {
        return new AuditTrailLogService(instanceLogsRestOperations, auditTrailProperties, objectMapper, yamlMapper);
    }

}
