package com.gds.batch.mapper;

import com.gds.entity.Product;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 04/10/2017
 */
public class ProductLoadingFieldSetMapper implements FieldSetMapper<Product> {

    @Override
    public Product mapFieldSet(final FieldSet fieldSet) throws BindException {

        final Product entity = new Product();
        entity.setSku(fieldSet.readString("sku"))
                .setDescription(fieldSet.readString("description"))
                .setManufacturer(fieldSet.readString("manufacturer"));
        return entity;
    }
}
