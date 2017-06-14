package com.cirrustech.demo.service.workflow.stepfunction.activities;

import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class GenerateNumberActivity extends BasicActivity implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateNumberActivity.class);
    private static final Random RAND = new Random();

    private final String activityArn;

    public GenerateNumberActivity(AWSStepFunctions client, String activityArn) {

        super(client);
        this.activityArn = activityArn;
    }

    @Override
    public void run() {

        while (true) {

            LOG.info("Starting activity {}", activityArn);
            GetActivityTaskResult getActivityTaskResult = fetchActivity(activityArn);
            if (getActivityTaskResult.getTaskToken() != null) {

                try {
                    int value = RAND.nextInt(10000);
                    LOG.info("Value generated is {}", value);

                    String result = encodeResponseToJSON("computedValue", value);
                    sendSuccessResult(getActivityTaskResult, result);

                } catch (Exception e) {

                    sendFailure(getActivityTaskResult);
                }
            }
        }

    }

}
