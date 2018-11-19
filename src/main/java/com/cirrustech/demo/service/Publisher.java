package com.cirrustech.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * Publisher class.
 */
@Component
public class Publisher {

    private static final Logger LOG = LoggerFactory.getLogger(Publisher.class);

    public static final String CHANNEL = "my-channel";
    private final Jedis jPublisher;

    public Publisher(@Value("${redis.host}") String host,
                     @Value("${redis.port}") int port) {

        jPublisher = new Jedis(host, port);
    }

    public void publish(String content) {

        LOG.info("Publishing {}.", content);
        jPublisher.publish(CHANNEL, content);
    }
}
