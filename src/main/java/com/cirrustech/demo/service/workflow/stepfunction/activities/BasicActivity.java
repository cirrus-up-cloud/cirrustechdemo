package com.cirrustech.demo.service.workflow.stepfunction.activities;

import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskRequest;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskResult;
import com.amazonaws.services.stepfunctions.model.SendTaskFailureRequest;
import com.amazonaws.services.stepfunctions.model.SendTaskSuccessRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class BasicActivity {

    private static final Logger LOG = LoggerFactory.getLogger(BasicActivity.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private final AWSStepFunctions client;

    public BasicActivity(AWSStepFunctions client) {

        this.client = client;
    }

    protected String encodeResponseToJSON(String id, int value) {

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set(id, new IntNode(value));

        try {

            return mapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {

            LOG.warn("Exception on serializing json  {} {} ", rootNode.toString(), e);

            throw new IllegalArgumentException("Invalid input");
        }
    }

    protected String encodeResponseToJSON(String id, boolean flag) {

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set(id, BooleanNode.valueOf(flag));

        try {

            return mapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {

            LOG.warn("Exception on serializing json  {} {} ", rootNode.toString(), e);

            throw new IllegalArgumentException("Invalid input");
        }
    }


    protected int getIntFieldFromJSON(String fieldName, String json) throws IOException {

        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);

            return node.get(fieldName).asInt();

        } catch (JsonProcessingException e) {

            LOG.warn("Exception on deserializing json  {} {} ", json, e);
            throw new IOException("Invalid input");
        }
    }

    protected List<Integer> getListOfIntFromJSON(String fieldName, String json) throws IOException {

        try {

            List<Integer> list = Lists.newArrayList();
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode nodes = (ArrayNode) mapper.readTree(json);

            for (JsonNode node : nodes) {

                list.add(node.get(fieldName).asInt());
            }

            return list;

        } catch (JsonProcessingException e) {

            LOG.warn("Exception on deserializing json  {} {} ", json, e);
            throw new IOException("Invalid input");
        }
    }


    protected GetActivityTaskResult fetchActivity(String activityArn) {
        return client.getActivityTask(new GetActivityTaskRequest()
                .withActivityArn(activityArn)
                .withSdkClientExecutionTimeout(70000));
    }

    protected void sendSuccessResult(GetActivityTaskResult getActivityTaskResult, String result) {

        client.sendTaskSuccess(new SendTaskSuccessRequest().withOutput(result).withTaskToken(getActivityTaskResult.getTaskToken()));
    }

    protected void sendFailure(GetActivityTaskResult getActivityTaskResult) {

        client.sendTaskFailure(new SendTaskFailureRequest().withTaskToken(getActivityTaskResult.getTaskToken()));
    }
}
