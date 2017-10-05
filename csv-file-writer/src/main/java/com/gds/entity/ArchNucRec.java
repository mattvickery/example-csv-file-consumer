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
@Table(name = "ARCH_NUC_REC")
@SequenceGenerator(name = "arch_nuc_rec_id_seq_gen", sequenceName = "ARCH_NUC_REC_ID_SEQ", allocationSize = 100)
public class ArchNucRec implements Serializable {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "arch_nuc_rec_id_seq_gen")
    private Long archNucRecId;

    @NotNull
    @Column(name = "SELLER_ID", nullable = false, length = 255)
    private String sellerId;

    @NotNull
    @Column(name = "IV_ID", nullable = false, length = 255)
    private String ivId;

    @NotNull
    @Column(name = "SHORT_NAME", nullable = false, length = 255)
    private String shortName;

    @NotNull
    @Column(name = "FULL_NAME", nullable = false, length = 255)
    private String fullName;

    @NotNull
    @Column(name = "COUNTRY", nullable = false, length = 255)
    private String country;

    @NotNull
    @Column(name = "status", nullable = false, length = 255)
    private String status;

    @NotNull
    @Column(name = "NUCLEUS_ID", nullable = false, length = 255)
    private String nucleusId;

    public ArchNucRec() {
    }

    public ArchNucRec setArchNucRecId(final Long archNucRecId) {
        this.archNucRecId = archNucRecId;
        return this;
    }

    public ArchNucRec setSellerId(final String sellerId) {
        this.sellerId = sellerId;
        return this;
    }

    public ArchNucRec setIvId(final String ivId) {
        this.ivId = ivId;
        return this;
    }

    public ArchNucRec setShortName(final String shortName) {
        this.shortName = shortName;
        return this;
    }

    public ArchNucRec setFullName(final String fullName) {
        this.fullName = fullName;
        return this;
    }

    public ArchNucRec setCountry(final String country) {
        this.country = country;
        return this;
    }

    public ArchNucRec setStatus(final String status) {
        this.status = status;
        return this;
    }

    public ArchNucRec setNucleusId(final String nucleusId) {
        this.nucleusId = nucleusId;
        return this;
    }

    public Long getArchNucRecId() {
        return archNucRecId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getIvId() {
        return ivId;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCountry() {
        return country;
    }

    public String getStatus() {
        return status;
    }

    public String getNucleusId() {
        return nucleusId;
    }
}
