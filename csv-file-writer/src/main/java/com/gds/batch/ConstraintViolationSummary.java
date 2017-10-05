package com.gds.batch;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public class ConstraintViolationSummary {

    private final String primaryKeyValue;
    private final String violationMessage;

    public ConstraintViolationSummary(final String primaryKeyValue,
                                      final String violationMessage) {

        this.primaryKeyValue = primaryKeyValue;
        this.violationMessage = violationMessage;
    }

    @Override
    public String toString() {
        return "ConstraintViolationSummary{" +
                ", primaryKeyValue='" + primaryKeyValue + '\'' +
                ", violationMessage='" + violationMessage + '\'' +
                '}';
    }
}
