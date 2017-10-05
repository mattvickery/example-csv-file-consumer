package com.gds.batch.listener;

import org.slf4j.Logger;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.util.StopWatch;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public class SpringBatchTimerListener {


    public final StopWatch watch = new StopWatch();
    private final Logger LOG = getLogger(SpringBatchTimerListener.class);

    @BeforeStep
    public void startWatch() {
        LOG.info("Starting StopWatch.");
        watch.start();
    }

    @AfterStep
    public void stopWatch() {
        watch.stop();
        LOG.info("StopWatch timing results:[{}ms]", watch.getLastTaskTimeMillis());
    }
}