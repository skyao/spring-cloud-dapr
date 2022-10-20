package io.dapr.spring.cloud.config.sample.configuration;

import java.util.ArrayList;
import java.util.List;

import io.dapr.client.DaprClientBuilder;
import io.dapr.client.DaprPreviewClient;
import io.dapr.client.domain.ConfigurationItem;
import io.dapr.client.domain.GetConfigurationRequest;
import io.dapr.client.domain.SubscribeConfigurationRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SampleApplication {
	private static final String CONFIG_STORE_NAME = "configstore";

	public static void main(String[] args) throws Exception {
		try (DaprPreviewClient client = (new DaprClientBuilder()).buildPreviewClient()) {
			List<String> keys = new ArrayList<>();
			keys.add("orderId1");
			keys.add("orderId2");
			GetConfigurationRequest req = new GetConfigurationRequest(CONFIG_STORE_NAME, keys);
			try {
				Mono<List<ConfigurationItem>> items = client.getConfiguration(req);
				items.block().forEach((item) -> {
					System.out.println(item.getValue() + " : key ->" + item.getKey());
				});
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

}
