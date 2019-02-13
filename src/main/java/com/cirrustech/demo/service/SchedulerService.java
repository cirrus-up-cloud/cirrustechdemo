package com.cirrustech.demo.service;

import com.github.davidmarquis.redisscheduler.RedisTaskScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Locking Service.
 */
@Component
public class SchedulerService {

    @Autowired
    private RedisTaskScheduler scheduler;

    public void schedule(String id, long delay) {

        scheduler.scheduleAt(id, Instant.ofEpochMilli(System.currentTimeMillis() + delay));
    }
}
