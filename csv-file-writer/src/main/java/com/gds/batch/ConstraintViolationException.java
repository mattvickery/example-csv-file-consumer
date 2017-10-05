package com.gds.batch;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public class ConstraintViolationException extends RuntimeException {

    private List<ConstraintViolationSummary> violationSummaries;

    public ConstraintViolationException(final String violationSummary) {
        super(violationSummary);
    }

    public List<ConstraintViolationException> convertSummariesToCollection() {

        final List<ConstraintViolationException> exceptions = new ArrayList<>(violationSummaries.size());
        violationSummaries.forEach(summary -> exceptions.add(new ConstraintViolationException(summary.toString())));
        return exceptions;
    }

    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        if (violationSummaries != null) {
            violationSummaries.forEach(summary -> builder.append(summary.toString()).append('\n'));
            return builder.toString();
        }
        return getMessage();
    }
}
