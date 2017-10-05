package com.gds.batch.listener;

import com.gds.batch.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.item.file.FlatFileParseException;

import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public class ValidationExceptionListener {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationExceptionListener.class);
    public final List<Throwable> throwables = new ArrayList<>();

    @OnReadError
    public void onReadError(Exception e) throws Exception {

        if (e instanceof FlatFileParseException) {
            final String specificErrorMessage = "Parse error at line [" + ((FlatFileParseException) e).getLineNumber()
                    + "], " + e.getCause().getMessage() + ", '" + ((FlatFileParseException) e).getInput() + "'";
            LOG.debug("Exception reported and skipped: {}", specificErrorMessage);
            throwables.add(new FlatFileParseException(specificErrorMessage, ((FlatFileParseException) e).getInput(),
                    ((FlatFileParseException) e).getLineNumber()));
        } else {
            throwables.add(e);
        }
    }

    @OnProcessError
    public <T extends Serializable> void onProcessError(T item, Exception throwable) {

        LOG.debug("Exception captured: " + throwable.getMessage());
        if (throwable instanceof ConstraintViolationException) {
            ((ConstraintViolationException) throwable).convertSummariesToCollection()
                    .forEach(exception -> throwables.add(exception));
        } else if (throwable instanceof PersistenceException) {
            final String cause = ((PersistenceException) throwable).getMessage();
        } else {
            throwables.add(throwable);
        }
    }

    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) throws Exception {

        if (throwables.size() > 0) {
            LOG.debug("Exception count [" + throwables.size() + "].");
            throwables.forEach(throwable -> stepExecution.addFailureException(throwable));
            return ExitStatus.FAILED;
        }
        return stepExecution.getExitStatus();
    }
}