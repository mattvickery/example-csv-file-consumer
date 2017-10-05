package com.gds;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 29/09/2017
 */
@SpringBootApplication
public class Launcher {
    public static void main(final String [] args) {
        new SpringApplicationBuilder(FileAggregatorConfiguration.class).web(false).run(args);
    }
}