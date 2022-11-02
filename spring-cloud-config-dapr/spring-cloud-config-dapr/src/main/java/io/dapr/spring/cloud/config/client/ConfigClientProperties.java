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

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * @author Dave Syer
 *
 */
@ConfigurationProperties(ConfigClientProperties.PREFIX)
public class ConfigClientProperties {

	/**
	 * Prefix for Spring Cloud Config properties.
	 */
	public static final String PREFIX = "spring.cloud.config";

	/**
	 * Default profile value.
	 */
	public static final String DEFAULT_PROFILE = "default";

	/**
	 * The default profile to use when fetching remote configuration (comma-separated).
	 * Default is "default".
	 */
	private String profile = DEFAULT_PROFILE;

	private List<String> keys;

	private String storeName;

	ConfigClientProperties() {
	}

	public ConfigClientProperties(Environment environment) {
		String[] profiles = environment.getActiveProfiles();
		if (profiles.length == 0) {
			profiles = environment.getDefaultProfiles();
		}
		this.setProfile(StringUtils.arrayToCommaDelimitedString(profiles));
	}

	public String getProfile() {
		return this.profile;
	}

	public void setProfile(String env) {
		this.profile = env;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreName() {
		return this.storeName;
	}

}
