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
@Table(name = "PRODUCT")
@SequenceGenerator(name = "product_id_seq_gen", sequenceName = "PRODUCT_ID_SEQ", allocationSize = 100)
public class Product implements Serializable {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq_gen")
    private Long productId;

    @NotNull
    @Column(name = "SKU", nullable = false, length = 255)
    private String sku;

    @NotNull
    @Column(name = "DESCRIPTION", nullable = false, length = 255)
    private String description;

    @NotNull
    @Column(name = "MANUFACTURER", nullable = false, length = 255)
    private String manufacturer;

    public Product() {
    }

    public Product setProductId(final Long nucRecId) {
        this.productId = nucRecId;
        return this;
    }

    public Product setSku(final String orgName) {
        this.sku = orgName;
        return this;
    }

    public Product setDescription(final String countryOfIncLongName) {
        this.description = countryOfIncLongName;
        return this;
    }

    public Product setManufacturer(final String orgStatus) {
        this.manufacturer = orgStatus;
        return this;
    }

    public Long getProductId() {
        return productId;
    }

    public String getSku() {
        return sku;
    }

    public String getDescription() {
        return description;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}
