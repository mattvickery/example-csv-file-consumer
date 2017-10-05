package com.gds.entity;

import org.springframework.batch.item.ItemProcessor;

import java.io.Serializable;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 04/10/2017
 */
public class EntityValidator <T extends Serializable> implements ItemProcessor<T, T> {

    @Override
    public T process(final T t) throws Exception {
        return t;
    }
}