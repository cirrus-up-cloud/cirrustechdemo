package com.cirrustech.demo.service.workflow.stepfunction.activities;

import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SumActivity extends BasicActivity implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(SumActivity.class);

    private final String activityArn;

    public SumActivity(AWSStepFunctions client, String activityArn) {

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
                    List<Integer> value = getListOfIntFromJSON("computedValue", getActivityTaskResult.getInput());
                    int total = value.get(0) + value.get(1);
                    LOG.info("Computed value is {}", total);

                    String result = encodeResponseToJSON("runAgain", true);
                    sendSuccessResult(getActivityTaskResult, result);

                } catch (Exception e) {

                    sendFailure(getActivityTaskResult);
                }
            }
        }
    }
}
