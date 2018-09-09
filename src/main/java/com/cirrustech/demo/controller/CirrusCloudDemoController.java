package com.cirrustech.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@ImportResource(locations = "classpath:spring-config/applicationContext.xml")
public class CirrusCloudDemoController {

    @Value("${default.text}")
    private String defaultText;

    private final QueueMessagingTemplate queueMessagingTemplate;
    private final String queueName;


    @Autowired
    public CirrusCloudDemoController(QueueMessagingTemplate queueMessagingTemplate,
                                     @Value("${sqs.queue.name}") String queueName) {

        this.queueMessagingTemplate = queueMessagingTemplate;
        this.queueName = queueName;
    }

    private static final Logger LOG = LoggerFactory.getLogger(CirrusCloudDemoController.class);

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {

        LOG.info("/hello request.");

        String message = "Hello, " + name + ". This is my first REST service! " + defaultText;
        queueMessagingTemplate.send(queueName, new GenericMessage<>(message));
        return message;
    }
}
