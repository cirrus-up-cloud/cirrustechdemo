package com.cirrustech.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.cirrustech.demo.service.Publisher.CHANNEL;

/**
 * Subscriber class.
 */
@Component
public class Subscriber {

    private static final Logger LOG = LoggerFactory.getLogger(Subscriber.class);

    private final Jedis jSubscriber;
    JedisPubSub subscriber = new JedisPubSub() {

        @Override
        public void onMessage(String channel, String message) {

            LOG.info("Message received: {}", message);
        }
    };

    public Subscriber(@Value("${redis.host}") String host,
                      @Value("${redis.port}") int port) {

        jSubscriber = new Jedis(host, port);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                jSubscriber.subscribe(subscriber, CHANNEL);
                LOG.info("Subscription ended.");
            } catch (Exception e) {
                LOG.error("Subscribing failed.", e);
            }
        });
    }
}
