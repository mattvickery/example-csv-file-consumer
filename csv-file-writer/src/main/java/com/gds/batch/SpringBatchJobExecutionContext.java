package com.gds.batch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.lang.String.valueOf;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public class SpringBatchJobExecutionContext implements JobExecutionContext {

    public static final String INPUT_FILE_CONVENTION = "_input_file";

    private ApplicationContext ctx;
    private JobParameters jobParams;
    private JobLauncher jobLauncher;
    private final Resource resource;
    private final String typeName;
    private Job job;

    public SpringBatchJobExecutionContext(final Resource resource,
                                          final String typeName) {
        this.resource = resource;
        this.typeName = typeName;
    }

    public JobExecutionContext resolve(final ApplicationContext ctx) {

        try {
            this.ctx = ctx;
            jobParams = jobParamsFrom(convertProperties());
            job = ctx.getBean(new StringBuilder(typeName).append("FileWriterJob").toString(), Job.class);
            jobLauncher = ctx.getBean(JobLauncher.class);
            return this;
        } catch (IOException e) {
            throw new IllegalStateException("Internal processing error:", e);
        }
    }

    @Override
    public JobParameters getJobParams() {
        return jobParams;
    }

    @Override
    public JobLauncher getJobLauncher() {
        return jobLauncher;
    }

    @Override
    public StepExecutionResponse executionResponseFrom(final BatchStatus batchStatus, final String message) {
        return new StepExecutionResponse(batchStatus, Arrays.asList(message));
    }

    @Override
    public StepExecutionResponse executionResponseFrom(final BatchStatus batchStatus, final List<String> messages) {
        return new StepExecutionResponse(batchStatus, messages);
    }

    @Override
    public Job getJob() {
        return job;
    }

    @Override
    public boolean resourcesValid() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public String resourceMissingErrorMessage() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public String resolveJobParam(final String key) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    private String convertToSpelFriendly(final String value) {
        return value.replace('.', '_');
    }

    private Properties convertProperties() throws IOException {

        final Properties jobProperties = ctx.getBean("batchParameters", Properties.class);
        final Properties convertedProperties = new Properties();
        jobProperties.keySet().stream().forEach(k -> {
            if ((((String) k).startsWith(typeName)) || (((String) k).startsWith("generic")))
                convertedProperties.put(convertToSpelFriendly((String)k), jobProperties.get(k));
            else if (((String) k).startsWith("db.")) {
                convertedProperties.put(k, jobProperties.get(k));
            }
        });
        if (resource instanceof FileSystemResource) {
            convertedProperties.setProperty(getTypeName() + INPUT_FILE_CONVENTION, resource.getFile().getAbsolutePath());
        } else if (resource instanceof ClassPathResource) {
            convertedProperties.setProperty(getTypeName() + INPUT_FILE_CONVENTION, resource.getURL().toString());
        } else {
            throw new IllegalStateException("Can't handle resources with type:" + resource.getClass());
        }

        return convertedProperties;
    }

    private JobParameters jobParamsFrom(final Properties properties) {

        final JobParametersBuilder builder = new JobParametersBuilder();
        properties.keySet().stream().forEach(k -> builder.addString(valueOf(k), valueOf(properties.get(k))));
        return builder.toJobParameters();
    }
}