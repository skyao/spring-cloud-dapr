package io.dapr.spring.cloud.stream.binder;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import org.apache.commons.lang3.RandomStringUtils;

@RestController
public class DaprHttpService {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  //private static final String ramdonPath = "/" + RandomStringUtils.randomAlphanumeric(20);
  //@Topic(name = "${spring.cloud.stream.bindings.supply-out-0.destination}", pubsubName = "${spring.cloud.dapr.bindings.supply-out-0:producer:pubsubName}")

  @Topic(name = "topic1", pubsubName = "kafka-pubsub")
  @PostMapping(path = "/ramdonpath" )
  public Mono<Void> handleMessage(@RequestBody(required = false) CloudEvent<?> cloudEvent) {
    return Mono.fromRunnable(() -> {
      try {
        System.out.println("Subscriber got: " + cloudEvent.getData());
        System.out.println("Subscriber got: " + OBJECT_MAPPER.writeValueAsString(cloudEvent));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }
}
