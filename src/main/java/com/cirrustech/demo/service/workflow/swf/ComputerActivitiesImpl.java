package com.cirrustech.demo.service.workflow.swf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class ComputerActivitiesImpl implements ComputerActivities {

    private static final Logger LOG = LoggerFactory.getLogger(ComputerActivitiesImpl.class);

    private final Random RAND = new Random();

    @Override
    public int generateNumber() {

        int value = RAND.nextInt(10000);
        LOG.info("Value generated is {}", value);

        return value;
    }

    @Override
    public int computeSquare(int value) {

        int sqr = value * value;
        LOG.info("For input value {}, square value is {}.", value, sqr);

        return sqr;
    }

    @Override
    public void sum(int v1, int v2) {

        int sum = v1 + v2;
        LOG.info("For input values {} and {} , sum is {}.", v1, v2, sum);
    }
}