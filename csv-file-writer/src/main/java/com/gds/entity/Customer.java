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
@Table(name = "CUSTOMER")
@SequenceGenerator(name = "customer_id_seq_gen", sequenceName = "CUSTOMER_ID_SEQ", allocationSize = 100)
public class Customer implements Serializable {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq_gen")
    private Long customerId;

    @NotNull
    @Column(name = "NAME", nullable = false, length = 255)
    private String name;

    @NotNull
    @Column(name = "EMAIL_ADDRESS", nullable = false, length = 128)
    private String emailAddress;

    @NotNull
    @Column(name = "COUNTRY", nullable = false, length = 255)
    private String country;

    @NotNull
    @Column(name = "STATUS", nullable = false, length = 255)
    private String status;

    public Customer() {
    }

    public Customer setCustomerId(final Long archNucRecId) {
        this.customerId = archNucRecId;
        return this;
    }

    public Customer setName(final String sellerId) {
        this.name = sellerId;
        return this;
    }

    public Customer setEmailAddress(final String ivId) {
        this.emailAddress = ivId;
        return this;
    }

    public Customer setCountry(final String country) {
        this.country = country;
        return this;
    }

    public Customer setStatus(final String status) {
        this.status = status;
        return this;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getCountry() {
        return country;
    }

    public String getStatus() {
        return status;
    }
}