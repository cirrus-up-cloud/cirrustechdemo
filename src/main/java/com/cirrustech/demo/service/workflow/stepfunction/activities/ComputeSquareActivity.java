package com.cirrustech.demo.service.workflow.stepfunction.activities;

import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComputeSquareActivity extends BasicActivity implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ComputeSquareActivity.class);

    private final String activityArn;

    public ComputeSquareActivity(AWSStepFunctions client, String activityArn) {

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
                    int value = getIntFieldFromJSON("value", getActivityTaskResult.getInput());
                    int total = value * value;
                    LOG.info("Computed value is {}", total);

                    String result = encodeResponseToJSON("computedValue", total);
                    sendSuccessResult(getActivityTaskResult, result);

                } catch (Exception e) {

                    sendFailure(getActivityTaskResult);
                }
            }
        }

    }


}
