package com.gds.batch;

import org.springframework.batch.core.BatchStatus;

import java.util.Collections;
import java.util.List;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public class StepExecutionResponse {

    private final BatchStatus batchStatus;
    private final List<String> exceptionMessages;

    public StepExecutionResponse(final BatchStatus batchStatus) {
        this(batchStatus, null);
    }

    public StepExecutionResponse(final BatchStatus batchStatus, final List<String> exceptionMessages) {
        this.batchStatus = batchStatus;
        this.exceptionMessages = exceptionMessages;
        Collections.sort(this.exceptionMessages);
    }

    public BatchStatus getBatchStatus() {
        return batchStatus;
    }

    public List<String> getExceptionMessages() {
        return exceptionMessages;
    }
}