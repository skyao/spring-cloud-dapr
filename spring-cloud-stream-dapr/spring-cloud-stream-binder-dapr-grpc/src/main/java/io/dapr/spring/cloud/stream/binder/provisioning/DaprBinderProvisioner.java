package io.dapr.spring.cloud.stream.binder.provisioning;


import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.cloud.stream.provisioning.ProvisioningException;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;

import io.dapr.spring.cloud.stream.binder.properties.DaprConsumerProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprProducerProperties;

/**
 * The {@link DaprBinderProvisioner} is responsible for the provisioning of consumer and producer destinations.
 */
public class DaprBinderProvisioner
		implements
		ProvisioningProvider<ExtendedConsumerProperties<DaprConsumerProperties>,
				ExtendedProducerProperties<DaprProducerProperties>> {

	@Override
	public ProducerDestination provisionProducerDestination(String name,
			ExtendedProducerProperties<DaprProducerProperties> properties) throws ProvisioningException {
		return new DaprProducerDestination(name);
	}

	@Override
	public ConsumerDestination provisionConsumerDestination(String name, String group,
			ExtendedConsumerProperties<DaprConsumerProperties> properties) throws ProvisioningException {
		return new DaprConsumerDestination(name);
	}

	private static final class DaprProducerDestination implements ProducerDestination {

		private final String topic;

		DaprProducerDestination(String topic) {
			this.topic = topic.trim();
		}

		@Override
		public String getName() {
			return topic;
		}

		@Override
		public String getNameForPartition(int partition) {
			return topic + "-" + partition;
		}
	}

	private static final class DaprConsumerDestination implements ConsumerDestination {

		private final String topic;

		DaprConsumerDestination(final String topic) {
			this.topic = topic.trim();
		}

		@Override
		public String getName() {
			return topic;
		}
	}
}
