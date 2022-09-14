package io.dapr.spring.cloud.stream.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.handler.AbstractMessageProducingHandler;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import io.dapr.client.DaprClient;
import io.dapr.client.domain.PublishEventRequest;

public class DaprMessageHandler extends AbstractMessageProducingHandler {
	private static Logger logger = LoggerFactory.getLogger(DaprMessageHandler.class);

	private final String topic;
	private final String pubsubName;
    private final DaprClient daprClient;

	/**
    * Construct a {@link DaprMessageHandler} with the specified topic and pubsubName.
    *
    * @param topic the topic
    * @param pubsubName the pubsub name
    */
   public DaprMessageHandler(String topic, String pubsubName, DaprClient daprClient) {
       Assert.hasText(topic, "topic can't be null or empty");
       Assert.hasText(pubsubName, "pubsubName can't be null or empty");
       this.topic = topic;
       this.pubsubName = pubsubName;
       this.daprClient = daprClient;
   }

    @Override
    protected void handleMessageInternal(Message<?> message) {
        PublishEventRequest request = new PublishEventRequest(pubsubName, topic, message.getPayload());
        this.daprClient.publishEvent(request).block();
        logger.info("succeed to send event " + message + "to " + pubsubName + "/"  +  topic);
    }
    
}
