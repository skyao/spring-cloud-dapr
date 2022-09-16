package io.dapr.spring.cloud.stream.sample.httpkafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class SubscriberController {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static Logger logger = LoggerFactory.getLogger(SubscriberController.class);

  @Topic(name = "topic1", pubsubName = "kafka-pubsub")
  @PostMapping(path = "/topic1")
  public Mono<Void> handleMessage(@RequestBody(required = false) CloudEvent<?> cloudEvent) {
    return Mono.fromRunnable(() -> {
      try {
        logger.info("Subscriber got: " + cloudEvent.getData());
        logger.info("Subscriber got: " + OBJECT_MAPPER.writeValueAsString(cloudEvent));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }
}
