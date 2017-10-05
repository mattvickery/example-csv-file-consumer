package com.gds.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public class CleanUpExitStatusListener {

    private static final Logger LOG = LoggerFactory.getLogger(CleanUpExitStatusListener.class);

    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) throws Exception {
        ExitStatus exitStatus = stepExecution.getExitStatus();
        for (StepExecution completedStepExecution : stepExecution.getJobExecution().getStepExecutions()) {
            if (! completedStepExecution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
                exitStatus = completedStepExecution.getExitStatus();
                break;
            }
        }
        return exitStatus;
    }
}