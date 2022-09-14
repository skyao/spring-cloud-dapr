package io.dapr.spring.cloud.stream.binder.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import io.dapr.spring.cloud.stream.binder.DaprHttpService;
import org.springframework.context.annotation.Bean;

/**
 * Dapr binder's Spring Boot AutoConfiguration for HTTP protocol.
 */
@Configuration
@ConditionalOnWebApplication
public class DaprHttpServerAutoConfiguration {

    @Bean
	@ConditionalOnMissingBean
	public DaprHttpService daprHttpService() {
		return new DaprHttpService();
	}
    
}
