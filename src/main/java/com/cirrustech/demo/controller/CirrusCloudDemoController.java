package com.cirrustech.demo.controller;

import com.cirrustech.demo.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@ImportResource(locations = "classpath:spring-config/applicationContext.xml")
public class CirrusCloudDemoController {

    @Value("${default.text}")
    private String defaultText;

    @Autowired
    private SchedulerService schedulerService;

    private static final Logger LOG = LoggerFactory.getLogger(CirrusCloudDemoController.class);

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {

        LOG.info("/hello request.");
        return "Hello, " + name + ". This is my first REST service! " + defaultText;
    }

    @PostMapping("/schedule")
    public ResponseEntity schedule(@RequestParam(value = "id", required = true) String id) {

        LOG.info("/schedule request.");

        schedulerService.schedule(id, 5000);
        return ResponseEntity.ok("Task scheduled.");
    }
}
