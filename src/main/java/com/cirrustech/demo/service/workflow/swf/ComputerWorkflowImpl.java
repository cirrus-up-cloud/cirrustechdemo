package com.cirrustech.demo.service.workflow.swf;

import com.amazonaws.services.simpleworkflow.flow.DecisionContextProvider;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClock;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.cirrustech.demo.service.workflow.WorkflowWatchDog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


public class ComputerWorkflowImpl implements ComputerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowWatchDog.class);
    public static final int DEFAULT_MAX_VALUE = 20;
    public static final int DEFAULT_WAIT_TIME = 300;

    private final ComputerActivitiesClient operations;
    private final DecisionContextProvider contextProvider;
    private final ComputerWorkflowSelfClient selfClient;
    private int maxValue = DEFAULT_MAX_VALUE;
    private int waitTime = DEFAULT_WAIT_TIME;

    private final Random rand = new Random();

    public ComputerWorkflowImpl(ComputerActivitiesClient operations, DecisionContextProvider contextProvider, ComputerWorkflowSelfClient selfClient) {

        this.operations = operations;
        this.contextProvider = contextProvider;
        this.selfClient = selfClient;
    }

    public void setMaxValue(int maxValue) {

        this.maxValue = maxValue;
    }

    public void setWaitTime(int waitTime) {

        this.waitTime = waitTime;
    }

    public void compute() {

        LOG.info("Running the workflow...");

        Promise<Integer> name = operations.generateNumber();
        int nr = rand.nextInt(maxValue);
        Promise<Integer> greeting = operations.computeSquare(nr);

        operations.sum(name, greeting);

        LOG.info("Workflow completed. Sleeping...");

        WorkflowClock clock = contextProvider.getDecisionContext().getWorkflowClock();
        Promise<Void> timer = clock.createTimer(waitTime);
        continueAsNew(timer);
    }

    @Asynchronous
    void continueAsNew(Promise<Void> timer) {

        LOG.info("Sleeping... {}", waitTime);
        selfClient.compute();
    }

}