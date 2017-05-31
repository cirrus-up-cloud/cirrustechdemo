package com.cirrustech.demo.service.workflow;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.model.ExecutionTimeFilter;
import com.amazonaws.services.simpleworkflow.model.ListOpenWorkflowExecutionsRequest;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecutionInfos;
import com.cirrustech.demo.service.workflow.swf.ComputerWorkflowClientExternal;
import com.cirrustech.demo.service.workflow.swf.ComputerWorkflowClientExternalFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.Executors;

public class WorkflowWatchDog {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowWatchDog.class);

    private final ComputerWorkflowClientExternalFactory factory;
    private final AmazonSimpleWorkflow client;
    private final String domain;
    private final String workflowId;

    public WorkflowWatchDog(ComputerWorkflowClientExternalFactory factory, AmazonSimpleWorkflow client, String domain, String workflowId) {

        this.factory = factory;
        this.client = client;
        this.domain = domain;
        this.workflowId = workflowId;
    }

    public void execute() throws Exception {

        LOG.info("Started WorkflowWatchDog execution... ");

        Executors.newSingleThreadExecutor().submit(new WorkflowWatchDogRunnable(factory, client, domain, workflowId));

        LOG.info("Runnable started.");
    }

    private final class WorkflowWatchDogRunnable implements Runnable {

        private final ComputerWorkflowClientExternalFactory factory;
        private final AmazonSimpleWorkflow client;
        private final String domain;
        private final String workflowId;

        public WorkflowWatchDogRunnable(ComputerWorkflowClientExternalFactory factory, AmazonSimpleWorkflow client, String domain, String workflowId) {
            this.factory = factory;
            this.client = client;
            this.domain = domain;
            this.workflowId = workflowId;
        }

        @Override
        public void run() {

            while (true) {

                synchronized (this) {

                    LOG.info("Check workflow is already running...");

                    Date oldestDate = new Date(System.currentTimeMillis() - 3600 * 1000);
                    ExecutionTimeFilter startTimeFilter = new ExecutionTimeFilter()
                            .withOldestDate(oldestDate);

                    ListOpenWorkflowExecutionsRequest request = new ListOpenWorkflowExecutionsRequest()
                            .withStartTimeFilter(startTimeFilter)
                            .withDomain(domain);

                    WorkflowExecutionInfos workflowExecutionInfos = client.listOpenWorkflowExecutions(request);

                    if (workflowExecutionInfos.getExecutionInfos().isEmpty()) {

                        LOG.info("Workflow {} is not running, starting now", workflowId);
                        ComputerWorkflowClientExternal computer = factory.getClient(new WorkflowExecution().withWorkflowId(workflowId));
                        computer.compute();

                    } else {

                        LOG.info("Workflow {} is already running.", workflowId);
                        sleep(60 * 1000);
                    }

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
