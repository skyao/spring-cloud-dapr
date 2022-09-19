package io.dapr.spring.cloud.stream.binder.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.binder.AbstractExtendedBindingProperties;
import org.springframework.cloud.stream.binder.BinderSpecificPropertiesProvider;

/**
 * The extended Dapr binding configuration properties.
 */
@ConfigurationProperties(prefix = "spring.cloud.stream.dapr")
public class DaprExtendedBindingProperties
		extends AbstractExtendedBindingProperties<DaprConsumerProperties,
		DaprProducerProperties, DaprBindingProperties> {

	private static final String DEFAULTS_PREFIX = "spring.cloud.stream.dapr.default";

	@Override
	public Class<? extends BinderSpecificPropertiesProvider> getExtendedPropertiesEntryClass() {
		return DaprBindingProperties.class;
	}

	@Override
	public Map<String, DaprBindingProperties> getBindings() {
		return this.doGetBindings();
	}

	@Override
	public String getDefaultsPrefix() {
		return DEFAULTS_PREFIX;
	}
}
