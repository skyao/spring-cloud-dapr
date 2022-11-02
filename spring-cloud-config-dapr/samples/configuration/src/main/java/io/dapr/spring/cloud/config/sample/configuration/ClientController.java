package io.dapr.spring.cloud.config.sample.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
public class ClientController {
    
    //inject value from dapr configuration
    @Value("${test}")
    private String test;

    @GetMapping(value = "/")
    public String getValue() {
        return "test:" + test + "\n";
    }

}
