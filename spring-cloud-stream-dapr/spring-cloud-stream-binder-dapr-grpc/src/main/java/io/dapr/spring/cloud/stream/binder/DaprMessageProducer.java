package io.dapr.spring.cloud.stream.binder;

import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.messaging.Message;
import io.dapr.spring.cloud.stream.binder.messaging.DaprMessageConverter;
import io.dapr.spring.cloud.stream.binder.DaprGrpcService;
import io.dapr.v1.DaprAppCallbackProtos;
import org.springframework.messaging.Message;
import io.dapr.v1.DaprProtos;

public class DaprMessageProducer extends MessageProducerSupport {
	private final DaprGrpcService daprGrpcService;
	private final DaprMessageConverter daprMessageConverter;

	public DaprMessageProducer(DaprGrpcService daprGrpcService,
			DaprMessageConverter daprMessageConverter,
			String pubsubName,
			String topic) {
		this.daprMessageConverter = daprMessageConverter;
		this.daprGrpcService = daprGrpcService;
		this.daprGrpcService.registerConsumer(pubsubName, topic,
				new DaprMessageConsumer(pubsubName, topic, this::onMessage));
	}

	private void onMessage(DaprAppCallbackProtos.TopicEventRequest request) {
		Message message = daprMessageConverter.toMessage(request);
		sendMessage(message);
	}

}
