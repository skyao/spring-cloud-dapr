package io.dapr.spring.cloud.stream.binder;

import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.messaging.Message;

public class DaprMessageProducer extends MessageProducerSupport {
    
    public DaprMessageProducer(String pubsubName, String topic) {

    }

    protected void sendMessage(Message<?> messageArg) {
        super.sendMessage(messageArg);
    }
}
