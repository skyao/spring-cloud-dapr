package io.dapr.spring.cloud.stream.binder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import io.dapr.spring.cloud.stream.binder.DaprGrpcService;

/**
 * Dapr binder's Spring Boot AutoConfiguration.
 */
@Configuration(proxyBeanMethods = false)
public class DaprGrpcServerAutoConfiguration {

    @Bean
	@ConditionalOnMissingBean
	public DaprGrpcService daprGrpcService() {
		return new DaprGrpcService();
	}
}
