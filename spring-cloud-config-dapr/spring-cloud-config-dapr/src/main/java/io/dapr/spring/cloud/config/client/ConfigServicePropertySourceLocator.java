/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dapr.spring.cloud.config.client;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.origin.Origin;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.annotation.Order;
import org.springframework.retry.annotation.Retryable;
import org.springframework.util.Assert;

/**
 * @author Dave Syer
 * @author Mathieu Ouellet
 *
 */
@Order(0)
public class ConfigServicePropertySourceLocator implements PropertySourceLocator {

	private static Log logger = LogFactory.getLog(ConfigServicePropertySourceLocator.class);


	private ConfigClientProperties defaultProperties;

	public ConfigServicePropertySourceLocator(ConfigClientProperties defaultProperties) {
		this.defaultProperties = defaultProperties;
	}

	@Override
	@Retryable(interceptor = "configServerRetryInterceptor")
	public org.springframework.core.env.PropertySource<?> locate(org.springframework.core.env.Environment environment) {
		return null;
	}

	@Override
	@Retryable(interceptor = "configServerRetryInterceptor")
	public Collection<org.springframework.core.env.PropertySource<?>> locateCollection(
			org.springframework.core.env.Environment environment) {
		return PropertySourceLocator.locateCollection(this, environment);
	}

	static class ConfigServiceOrigin implements Origin {

		private final String remotePropertySource;

		private final Object origin;

		ConfigServiceOrigin(String remotePropertySource, Object origin) {
			this.remotePropertySource = remotePropertySource;
			Assert.notNull(origin, "origin may not be null");
			this.origin = origin;

		}

		@Override
		public String toString() {
			return "Config Server " + this.remotePropertySource + ":" + this.origin.toString();
		}

	}

}
