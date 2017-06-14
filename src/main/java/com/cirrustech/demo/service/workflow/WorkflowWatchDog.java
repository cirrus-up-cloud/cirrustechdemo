package com.cirrustech.demo.service.workflow;

import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.ExecutionListItem;
import com.amazonaws.services.stepfunctions.model.ListExecutionsRequest;
import com.amazonaws.services.stepfunctions.model.ListExecutionsResult;
import com.amazonaws.services.stepfunctions.model.StartExecutionRequest;
import com.amazonaws.services.stepfunctions.model.StopExecutionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkflowWatchDog {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowWatchDog.class);
    private static final Random RAND = new Random();

    private final String stateArn;
    private final AWSStepFunctions client;
    private final List<Runnable> runs;


    public WorkflowWatchDog(String stateArn, AWSStepFunctions client, List<Runnable> runs) {

        this.stateArn = stateArn;
        this.client = client;
        this.runs = runs;
    }

    public void execute() throws Exception {

        LOG.info("Started WorkflowWatchDog execution... ");
        ExecutorService executor = Executors.newFixedThreadPool(runs.size() + 1);

        executor.submit(new WorkflowWatchDogRunnable(client, stateArn));

        for (Runnable runnable : runs) {

            executor.submit(runnable);
        }

        LOG.info("Runnable started.");
    }

    public void shutDown() {

        ListExecutionsRequest request = new ListExecutionsRequest()
                .withStateMachineArn(stateArn)
                .withStatusFilter("RUNNING");

        ListExecutionsResult result = client.listExecutions(request);
        if (!result.getExecutions().isEmpty()) {

            for (ExecutionListItem item : result.getExecutions()) {

                client.stopExecution(new StopExecutionRequest()
                        .withExecutionArn(item.getExecutionArn())
                        .withCause("Destroy Spring content"));
            }
        }


    }

    private final class WorkflowWatchDogRunnable implements Runnable {

        private final String stateArn;
        private final AWSStepFunctions client;

        public WorkflowWatchDogRunnable(AWSStepFunctions client, String stateArn) {

            this.stateArn = stateArn;
            this.client = client;
        }

        @Override
        public void run() {

            while (true) {

                synchronized (this) {

                    LOG.info("Check state is already running... {}", stateArn);

                    ListExecutionsRequest request = new ListExecutionsRequest()
                            .withStateMachineArn(stateArn)
                            .withStatusFilter("RUNNING");

                    ListExecutionsResult result = client.listExecutions(request);
                    if (result.getExecutions().isEmpty()) {

                        LOG.info("State {} is not running, starting now", stateArn);
                        StartExecutionRequest runRequest = new StartExecutionRequest();
                        runRequest.setStateMachineArn(stateArn);
                        runRequest.setInput(String.format("{\n" +
                                "    \"value\": %d\n" +
                                "}", RAND.nextInt(1000)));

                        client.startExecution(runRequest);
                    }

                    sleep(60 * 1000);
                }
            }

        }

        private void sleep(int interval) {

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
            }
        }
    }
}
