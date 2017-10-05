package com.gds.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
@Entity
@Table(name = "NUCLEUS_REC")
@SequenceGenerator(name = "nuc_rec_id_seq_gen", sequenceName = "NUC_REC_ID_SEQ", allocationSize = 100)
public class NucleusRec implements Serializable {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nuc_rec_id_seq_gen")
    private Long nucRecId;

    @NotNull
    @Column(name = "ORG_ID", nullable = false, length = 255)
    private String orgId;

    @NotNull
    @Column(name = "ORG_NAME", nullable = false, length = 255)
    private String orgName;

    @NotNull
    @Column(name = "COUNTRY_OF_INC_LONG_NAME", nullable = false, length = 255)
    private String countryOfIncLongName;

    @NotNull
    @Column(name = "ORG_STATUS", nullable = false, length = 255)
    private String orgStatus;

    @NotNull
    @Column(name = "LINK_ORG_ID", nullable = false, length = 255)
    private String linkOrgId;

    @NotNull
    @Column(name = "LINK_ORG_STATUS", nullable = false, length = 255)
    private String linkOrgStatus;

    @NotNull
    @Column(name = "LINK_ORG_ID_SCHEME", nullable = false, length = 255)
    private String linkOrgIdScheme;

    @NotNull
    @Column(name = "LINK_TYPE", nullable = false, length = 255)
    private String linkType;

    @NotNull
    @Column(name = "ORG_ALT_ID_SYSTEM", nullable = false, length = 255)
    private String orgAltIdSystem;

    @NotNull
    @Column(name = "ORG_ALT_ID", nullable = false, length = 255)
    private String orgAltId;

    public NucleusRec() {
    }

    public NucleusRec setNucRecId(final Long nucRecId) {
        this.nucRecId = nucRecId;
        return this;
    }

    public NucleusRec setOrgId(final String orgId) {
        this.orgId = orgId;
        return this;
    }

    public NucleusRec setOrgName(final String orgName) {
        this.orgName = orgName;
        return this;
    }

    public NucleusRec setCountryOfIncLongName(final String countryOfIncLongName) {
        this.countryOfIncLongName = countryOfIncLongName;
        return this;
    }

    public NucleusRec setOrgStatus(final String orgStatus) {
        this.orgStatus = orgStatus;
        return this;
    }

    public NucleusRec setLinkOrgId(final String linkOrgId) {
        this.linkOrgId = linkOrgId;
        return this;
    }

    public NucleusRec setLinkOrgStatus(final String linkOrgStatus) {
        this.linkOrgStatus = linkOrgStatus;
        return this;
    }

    public NucleusRec setLinkOrgIdScheme(final String linkOrgIdScheme) {
        this.linkOrgIdScheme = linkOrgIdScheme;
        return this;
    }

    public NucleusRec setLinkType(final String linkType) {
        this.linkType = linkType;
        return this;
    }

    public NucleusRec setOrgAltIdSystem(final String orgAltIdSystem) {
        this.orgAltIdSystem = orgAltIdSystem;
        return this;
    }

    public NucleusRec setOrgAltId(final String orgAltId) {
        this.orgAltId = orgAltId;
        return this;
    }

    public Long getNucRecId() {
        return nucRecId;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getCountryOfIncLongName() {
        return countryOfIncLongName;
    }

    public String getOrgStatus() {
        return orgStatus;
    }

    public String getLinkOrgId() {
        return linkOrgId;
    }

    public String getLinkOrgStatus() {
        return linkOrgStatus;
    }

    public String getLinkOrgIdScheme() {
        return linkOrgIdScheme;
    }

    public String getLinkType() {
        return linkType;
    }

    public String getOrgAltIdSystem() {
        return orgAltIdSystem;
    }

    public String getOrgAltId() {
        return orgAltId;
    }
}
