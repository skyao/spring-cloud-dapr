package io.dapr.spring.cloud.stream.sample.localkafka;


import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootApplication
public class SampleApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleApplication.class);

    public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}

    private int i = 0;

	@Bean
	public Supplier<Message<String>> supply() {
		return () -> {
			LOGGER.info("Sending message, sequence " + i++);
			return MessageBuilder.withPayload("event body").build();
		};
	}
}
