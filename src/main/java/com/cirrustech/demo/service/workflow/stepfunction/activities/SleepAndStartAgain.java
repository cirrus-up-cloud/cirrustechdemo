package com.cirrustech.demo.service.workflow.stepfunction.activities;

import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class SleepAndStartAgain extends BasicActivity implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ComputeSquareActivity.class);
    private static final Random RAND = new Random();

    private final String activityArn;
    private final long sleepTimeSeconds;

    public SleepAndStartAgain(AWSStepFunctions client, String activityArn, long sleepTimeSeconds) {

        super(client);
        this.activityArn = activityArn;
        this.sleepTimeSeconds = sleepTimeSeconds;
    }

    @Override
    public void run() {

        while (true) {

            LOG.info("Starting activity {}", activityArn);
            GetActivityTaskResult getActivityTaskResult = fetchActivity(activityArn);
            if (getActivityTaskResult.getTaskToken() != null) {

                try {
                    LOG.info("Start sleeping for {} seconds.", sleepTimeSeconds);
                    Thread.sleep(sleepTimeSeconds * 1000);
                } catch (InterruptedException e) {

                    LOG.error("Exception on sleep {}", e);
                }
                String result = String.format("{\n" +
                        "    \"value\": %d\n" +
                        "}", RAND.nextInt(100));

                sendSuccessResult(getActivityTaskResult, result);
                LOG.info("Re-run the whole state machine...");
            }
        }
    }
}
