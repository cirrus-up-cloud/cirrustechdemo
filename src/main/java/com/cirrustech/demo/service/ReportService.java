package com.cirrustech.demo.service;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Locking Service.
 */
@Component
public class ReportService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportService.class);

    private RedissonClient redisson;

    public ReportService(@Value("${redis.cluster.address}") String clusterAddress) {

        Config config = new Config();
        config.useSingleServer().setAddress(clusterAddress);

        redisson = Redisson.create(config);
    }

    public void generateReport(String client) {

        RLock lock = redisson.getLock(client);
        try {
            boolean result = lock.tryLock(3, SECONDS);
            LOG.info("Attempting to get lock for client {}. Result {}", client, result);
            if (!result) {

                throw new IllegalArgumentException("Report for client " + client + " already started.");
            }
            LOG.info("Starting to generate report.");
            Thread.sleep(30 * 1000);
            LOG.info("Report completed.");
            lock.unlock();

        } catch (InterruptedException e) {

            LOG.warn("Exception on getting lock for client {} ", client, e);
            throw new IllegalStateException("Internal error.");
        }
    }
}
