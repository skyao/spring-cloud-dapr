package io.dapr.spring.cloud.stream.binder.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;

/**
 * Dapr binder's Spring Boot AutoConfiguration for HTTP protocol.
 */
@Configuration
@ConditionalOnWebApplication
public class DaprHttpServerAutoConfiguration {
    
}
