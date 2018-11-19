package com.cirrustech.demo.controller;

import com.cirrustech.demo.service.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@ImportResource(locations = "classpath:spring-config/applicationContext.xml")
public class CirrusCloudDemoController {

    private final Publisher publisher;
    private final String defaultText;

    public CirrusCloudDemoController(Publisher publisher,
                                     @Value("${default.text}") String defaultText) {

        this.publisher = publisher;
        this.defaultText = defaultText;
    }

    private static final Logger LOG = LoggerFactory.getLogger(CirrusCloudDemoController.class);

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {

        LOG.info("/hello request.");
        publisher.publish(name);
        return "Hello, " + name + ". This is my first REST service! " + defaultText;
    }
}
