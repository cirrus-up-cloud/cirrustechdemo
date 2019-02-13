package com.cirrustech.demo.task;

import com.github.davidmarquis.redisscheduler.TaskTriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task trigger.
 */
public class CirrusTaskTriggerListener implements TaskTriggerListener {

    private static final Logger LOG = LoggerFactory.getLogger(CirrusTaskTriggerListener.class);


    @Override
    public void taskTriggered(String taskId) {

        LOG.info("Task triggered: " + taskId);
    }
}
