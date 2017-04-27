package com.cirrustech.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CirrusCloudDemo {

    private static final Logger LOG = LoggerFactory.getLogger(CirrusCloudDemo.class);

    public static void main(String[] args) {

        LOG.info("Starting application... ");
        SpringApplication.run(CirrusCloudDemo.class, args);
    }
}
