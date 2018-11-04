package com.cirrustech.demo.controller;

import com.cirrustech.demo.service.ThrottlingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@ImportResource(locations = "classpath:spring-config/applicationContext.xml")
public class CirrusCloudDemoController {

    @Value("${default.text}")
    private String defaultText;

    private static final Logger LOG = LoggerFactory.getLogger(CirrusCloudDemoController.class);

    @Autowired
    private ThrottlingService service;

    @GetMapping("/hello")
    public ResponseEntity hello(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {

        LOG.info("/hello request.");

        if (!service.tryConsume(name)) {

            return ResponseEntity.status(429).body("Too many calls!");
        }

        return ResponseEntity.ok().body("Hello, " + name + ". This is my first REST service!" + defaultText);
    }
}
