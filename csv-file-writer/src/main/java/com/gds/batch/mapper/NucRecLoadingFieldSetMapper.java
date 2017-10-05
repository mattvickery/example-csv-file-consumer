package com.gds.batch.mapper;

import com.gds.entity.NucleusRec;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 04/10/2017
 */
public class NucRecLoadingFieldSetMapper implements FieldSetMapper<NucleusRec> {

    @Override
    public NucleusRec mapFieldSet(final FieldSet fieldSet) throws BindException {

        final NucleusRec entity = new NucleusRec();
        entity.setOrgId(fieldSet.readString("orgid"))
                .setOrgName(fieldSet.readString("orgname"))
                .setCountryOfIncLongName(fieldSet.readString("countryofincname"))
                .setOrgStatus(fieldSet.readString("orgstatus"))
                .setLinkOrgId(fieldSet.readString("linkorgid"))
                .setLinkOrgStatus(fieldSet.readString("linkordstatus"))
                .setLinkOrgIdScheme(fieldSet.readString("linkorgidscheme"))
                .setLinkType(fieldSet.readString("linktype"))
                .setOrgAltIdSystem(fieldSet.readString("orgaltidsystem"))
                .setOrgAltId(fieldSet.readString("orgaltid"));
        return entity;
    }
}
