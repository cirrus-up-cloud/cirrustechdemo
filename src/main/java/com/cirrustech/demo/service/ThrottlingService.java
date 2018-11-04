package com.cirrustech.demo.service;

import com.coveo.spillway.Spillway;
import com.coveo.spillway.SpillwayFactory;
import com.coveo.spillway.exception.SpillwayLimitExceededException;
import com.coveo.spillway.limit.Limit;
import com.coveo.spillway.limit.LimitBuilder;
import com.coveo.spillway.storage.AsyncLimitUsageStorage;
import com.coveo.spillway.storage.LimitUsageStorage;
import com.coveo.spillway.storage.RedisStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.time.Duration;

/**
 * Throttling service.
 */
@Component
public class ThrottlingService {

    private final SpillwayFactory spillwayFactory;
    private final Limit<String> limits;

    public ThrottlingService(@Value("${throttle.host}") String host,
                             @Value("${throttle.port}") int port) {
        
        RedisStorage builder = RedisStorage.builder().withJedisPool(new JedisPool(host, port)).build();
        LimitUsageStorage storage = new AsyncLimitUsageStorage(builder);
        spillwayFactory = new SpillwayFactory(storage);

        limits = LimitBuilder.of("limits").to(2).per(Duration.ofMinutes(1)).build();
    }

    public boolean tryConsume(String value) {

        Spillway<String> spillway = spillwayFactory.enforce(value, limits);
        try {
            spillway.call(value);
            return true;
        } catch (SpillwayLimitExceededException e) {

            return false;
        }
    }
}
