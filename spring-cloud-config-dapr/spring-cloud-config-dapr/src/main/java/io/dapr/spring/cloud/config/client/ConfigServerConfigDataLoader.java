/*
 * Copyright 2013-2020 the original author or authors.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigData.Option;
import org.springframework.boot.context.config.ConfigData.Options;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.boot.origin.Origin;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.Ordered;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;

import io.dapr.client.DaprClientBuilder;
import io.dapr.client.DaprPreviewClient;
import io.dapr.client.domain.ConfigurationItem;
import io.dapr.client.domain.GetConfigurationRequest;
import io.dapr.spring.cloud.config.environment.Environment;
import reactor.core.publisher.Mono;

public class ConfigServerConfigDataLoader implements ConfigDataLoader<ConfigServerConfigDataResource>, Ordered {

	/**
	 * PropertySource name for the config client.
	 */
	public static final String CONFIG_CLIENT_PROPERTYSOURCE_NAME = "configClient";

	private static final EnumSet<Option> ALL_OPTIONS = EnumSet.allOf(Option.class);

	protected final Log logger;

	public ConfigServerConfigDataLoader(DeferredLogFactory logFactory) {
		this.logger = logFactory.getLog(getClass());
	}

	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public ConfigData load(ConfigDataLoaderContext context, ConfigServerConfigDataResource resource) {
		return doLoad(context, resource);
	}

	public ConfigData doLoad(ConfigDataLoaderContext context, ConfigServerConfigDataResource resource) {
		ConfigClientProperties properties = resource.getProperties();
		List<PropertySource<?>> propertySources = new ArrayList<>();
		Exception error = null;
		String errorBody = null;
		try {
			try (DaprPreviewClient client = (new DaprClientBuilder()).buildPreviewClient()) {
				GetConfigurationRequest req = new GetConfigurationRequest(properties.getStoreName(),properties.getKeys());
				Map<String, Object> seqMap = new LinkedHashMap<>();
				try {
					Mono<List<ConfigurationItem>> items = client.getConfiguration(req);
					items.block().forEach((item) -> {
						seqMap.put(item.getKey(), item.getValue());
					});

					@SuppressWarnings("unchecked")
					Map<String, Object> m = translateOrigins("configuration items", seqMap);
					propertySources.add(0, new OriginTrackedMapPropertySource(
							"dapr configuration items", m));

				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}

			HashMap<String, Object> map = new HashMap<>();

			// the existence of this property source confirms a successful
			// response from config server
			propertySources.add(0, new MapPropertySource(CONFIG_CLIENT_PROPERTYSOURCE_NAME, map));
			if (ALL_OPTIONS.size() == 1) {
				// boot 2.4.2 and prior
				return new ConfigData(propertySources);
			} else if (ALL_OPTIONS.size() == 2) {
				// boot 2.4.3 and 2.4.4
				return new ConfigData(propertySources, Option.IGNORE_IMPORTS, Option.IGNORE_PROFILES);
			} else if (ALL_OPTIONS.size() > 2) {
				// boot 2.4.5+
				return new ConfigData(propertySources, propertySource -> {
					String propertySourceName = propertySource.getName();
					List<Option> options = new ArrayList<>();
					options.add(Option.IGNORE_IMPORTS);
					options.add(Option.IGNORE_PROFILES);
					// TODO: the profile is now available on the backend
					// in a future minor, add the profile associated with a
					// PropertySource see
					// https://github.com/spring-cloud/spring-cloud-config/issues/1874
					for (String profile : resource.getAcceptedProfiles()) {
						// TODO: switch to match
						// , is used as a profile-separator for property sources
						// from vault
						// - is the default profile-separator for property sources
						if (propertySourceName.matches(".*[-,]" + profile + ".*")) {
							// // TODO: switch to Options.with() when implemented
							options.add(Option.PROFILE_SPECIFIC);
						}
					}
					return Options.of(options.toArray(new Option[0]));
				});
			}
			// errorBody = String.format("None of labels %s found",
		} catch (HttpServerErrorException e) {
			error = e;
			if (MediaType.APPLICATION_JSON.includes(e.getResponseHeaders().getContentType())) {
				errorBody = e.getResponseBodyAsString();
			}
		} catch (Exception e) {
			error = e;
		}
		logger.warn("Could not locate PropertySource (" + resource + "): "
				+ (error != null ? error.getMessage() : errorBody));
		return null;
	}

	protected void log(Environment result) {
		if (logger.isInfoEnabled()) {
			logger.info(String.format("Located environment: name=%s, profiles=%s, label=%s, version=%s, state=%s",
					result.getName(), result.getProfiles() == null ? "" : Arrays.asList(result.getProfiles()),
					result.getLabel(), result.getVersion(), result.getState()));
		}
		if (logger.isDebugEnabled()) {
			List<io.dapr.spring.cloud.config.environment.PropertySource> propertySourceList = result
					.getPropertySources();
			if (propertySourceList != null) {
				int propertyCount = 0;
				for (io.dapr.spring.cloud.config.environment.PropertySource propertySource : propertySourceList) {
					propertyCount += propertySource.getSource().size();
				}
				logger.debug(String.format("Environment %s has %d property sources with %d properties.",
						result.getName(), result.getPropertySources().size(), propertyCount));
			}

		}
	}

	protected Map<String, Object> translateOrigins(String name, Map<String, Object> source) {
		Map<String, Object> withOrigins = new LinkedHashMap<>();
		for (Map.Entry<String, Object> entry : source.entrySet()) {
			boolean hasOrigin = false;

			if (entry.getValue() instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> value = (Map<String, Object>) entry.getValue();
				if (value.size() == 2 && value.containsKey("origin") && value.containsKey("value")) {
					Origin origin = new ConfigServicePropertySourceLocator.ConfigServiceOrigin(name,
							value.get("origin"));
					OriginTrackedValue trackedValue = OriginTrackedValue.of(value.get("value"), origin);
					withOrigins.put(entry.getKey(), trackedValue);
					hasOrigin = true;
				}
			}

			if (!hasOrigin) {
				withOrigins.put(entry.getKey(), entry.getValue());
			}
		}
		return withOrigins;
	}

	protected void putValue(HashMap<String, Object> map, String key, String value) {
		if (StringUtils.hasText(value)) {
			map.put(key, value);
		}
	}

}
