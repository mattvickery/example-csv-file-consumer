package com.gds.batch;

import org.slf4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemStreamException;

import java.util.List;

import static com.gds.batch.BatchExceptionHandler.collateExceptionMessages;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.Assert.notNull;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public class SpringBatchJobExecutor {

    private static final Logger LOG = getLogger(SpringBatchJobExecutor.class);

    public StepExecutionResponse execute(final JobExecutionContext ctx) {

        notNull(ctx, "Mandatory argument 'ctx' is null.");
        try {
            final JobExecution execution = ctx.getJobLauncher().run(ctx.getJob(), ctx.getJobParams());
            LOG.info("JobExecution exit status: {}", execution.getStatus());
            final List<String> exceptionMessages =
                    collateExceptionMessages(ctx.getJob().getName(), execution);
            return ctx.executionResponseFrom(execution.getStatus(), exceptionMessages);
        } catch (final ItemStreamException e) {
            if (e.getMessage().contains("Failed to initialize the reader"))
                throw new IllegalStateException("Error working with supplied input file, please check " +
                        "that it is the correct type and is not corrupt.", e);
            throw e;
        } catch (JobExecutionAlreadyRunningException | JobRestartException
                | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new IllegalStateException(e);
        }
    }
}