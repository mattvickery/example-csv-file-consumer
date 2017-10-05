package com.gds.batch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.List;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public interface JobExecutionContext {

    /**
     * Spring Batch job parameter values.
     *
     * @return
     */
    JobParameters getJobParams();

    /**
     * Access to the Spring Batch launcher job, this can be obtained elsewhere if necessary, this
     * is merely a convenient accessor to that object.
     *
     * @return
     */
    JobLauncher getJobLauncher();

    /**
     * A convenience method that builds a step execution response from a batch status message and
     * a user supplied message, useful for executors that don't want to do the work themselves and
     * also require a consistent batch response format.
     *
     * @param batchStatus status of the batch job.
     * @param message     a user supplied message.
     * @return a uniform representation of a step execution response.
     */
    StepExecutionResponse executionResponseFrom(final BatchStatus batchStatus, final String message);

    /**
     * A convenience method that builds a step execution response from a batch status message and
     * a user supplied message, useful for executors that don't want to do the work themselves and
     * also require a consistent batch response format.
     *
     * @param batchStatus status of the batch job.
     * @param messages    a set of user supplied messages.
     * @return a uniform representation of a step execution response.
     */
    StepExecutionResponse executionResponseFrom(final BatchStatus batchStatus, final List<String> messages);

    /**
     * Access to the single job in each batch context that wraps all steps for a defined batch strategy.
     *
     * @return a Spring Batch job service object.
     */
    Job getJob();

    /**
     * Are the file resources supplied to the Spring Batch job valid for processing, i.e. readable,
     * non-corrupted and locatable?
     *
     * @return true if valid, false otherwise.
     */
    boolean resourcesValid();

    /**
     * A message for execution context users to use in the case of missing resources, just a convenient
     * shortcut to a standard message and context information.
     *
     * @return an error message.
     */
    String resourceMissingErrorMessage();

    /**
     * Given a key, resolve a Spring Batch job parameter.
     *
     * @param key lookup for job parameter value.
     * @return a value that has been set for the 'key' parameter.
     */
    String resolveJobParam(final String key);

    /**
     * Return the name of the resource name that's undergoing processing whilst this context is used.
     *
     * @return a resource name.
     */
    String getTypeName();
}
