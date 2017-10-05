package com.gds.batch.mapper;

import com.gds.entity.ArchNucRec;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public class ArchNucRecLoadingFieldSetMapper implements FieldSetMapper<ArchNucRec> {

    @Override
    public ArchNucRec mapFieldSet(final FieldSet fieldSet) throws BindException {

        final ArchNucRec entity = new ArchNucRec();
        entity.setSellerId(fieldSet.readString("seller_id"))
                .setIvId(fieldSet.readString("iv_id"))
                .setShortName(fieldSet.readString("short_name"))
                .setFullName(fieldSet.readString("full_name"))
                .setCountry(fieldSet.readString("country"))
                .setStatus(fieldSet.readString("status"))
                .setNucleusId(fieldSet.readString("nucleus_id"));
        return entity;
    }
}