package com.gds.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.util.Assert.notNull;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public class BatchExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(BatchExceptionHandler.class);

    public static List<String> collateExceptionMessages(final String jobName, final JobExecution execution) {

        notNull(jobName, "Mandatory argument 'jobName' is null.");
        notNull(execution, "Mandatory argument 'execution' is null.");

        if (execution.getStatus() != BatchStatus.FAILED) {
            return Collections.emptyList();
        } else {
            final List<String> messages = new ArrayList<>();
            final List<Throwable> exceptions = execution.getAllFailureExceptions();
            if (exceptions == null || exceptions.size() < 1) {
                final String message = format("[%s] batch job failed but with no exceptions.", jobName);
                log.debug(message);
                messages.add(message);
            } else {
                for (Throwable e : exceptions) {
                    log.debug(format("[{}] batch job failed with exception:{}", jobName, e.getMessage()));
                    if (! (e.getMessage().startsWith("Skip limit of ")))
                        messages.add(e.getMessage());
                }
            }
            return messages;
        }
    }
}