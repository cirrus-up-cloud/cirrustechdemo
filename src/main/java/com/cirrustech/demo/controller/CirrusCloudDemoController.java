package com.cirrustech.demo.controller;

import com.cirrustech.demo.datastore.FileDataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Configuration
@ImportResource(locations = "classpath:spring-config/applicationContext.xml")
public class CirrusCloudDemoController {

    @Value("${default.text}")
    private String defaultText;

    @Autowired
    private FileDataStore fileDataStore;

    private static final Logger LOG = LoggerFactory.getLogger(CirrusCloudDemoController.class);

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {

        LOG.info("/hello request.");
        return "Hello, " + name + ". This is my first REST service! " + defaultText;
    }

    @RequestMapping(value = "/upload", method = POST)
    public ResponseEntity upload(@RequestPart(required = true) MultipartFile file) {

        if (!file.isEmpty()) {
            try {
                fileDataStore.addFile(file.getOriginalFilename(), file.getBytes());
            } catch (Exception e) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        return ResponseEntity.ok().build();
    }
}
