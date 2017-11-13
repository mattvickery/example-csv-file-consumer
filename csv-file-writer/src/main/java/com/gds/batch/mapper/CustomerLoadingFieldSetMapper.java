package com.gds.batch.mapper;

import com.gds.entity.Customer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public class CustomerLoadingFieldSetMapper implements FieldSetMapper<Customer> {

    @Override
    public Customer mapFieldSet(final FieldSet fieldSet) throws BindException {

        final Customer entity = new Customer();
        entity.setName(fieldSet.readString("name"))
                .setEmailAddress(fieldSet.readString("email_address"))
                .setCountry(fieldSet.readString("country"))
                .setStatus(fieldSet.readString("status"));
        return entity;
    }
}