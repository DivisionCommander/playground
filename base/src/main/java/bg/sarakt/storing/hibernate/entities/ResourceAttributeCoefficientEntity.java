/*
 * ResourceAttributeCoefficientEntity.java
 *
 * created at 2024-01-21 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.storing.hibernate.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "tbl_attribute_coefficients")
public class ResourceAttributeCoefficientEntity implements Serializable{

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 2024201210204L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="coefficient_id")
    private long coefficientId;
    @Column(name= "attribute_id", insertable =false, unique = false, updatable = false)
    private long attributeId;
    @Column (name = "level")
    private int level;
    @Column (name = "coefficient")
    private BigDecimal coefficient;

    @ManyToOne
    @JoinColumn(name="attribute_id", nullable = false)
    private ResourceAttributeEntity resourceAttributeEntity;

    public ResourceAttributeCoefficientEntity() {}
    public ResourceAttributeCoefficientEntity(long coefficientId, long attributeId, int level, BigDecimal coefficient) {
        super();
        this.coefficientId = coefficientId;
        this.attributeId = attributeId;
        this.level = level;
        this.coefficient = coefficient;//.toPlainString();
    }


    public long getCoefficientId() { return coefficientId; }


    public long getAttributeId() { return attributeId; }


    public int getLevel() { return level; }


    public BigDecimal getCoefficient() { return coefficient; }


    public void setCoefficientId(long coefficientId) { this.coefficientId = coefficientId; }


    public void setAttributeId(long attributeId) { this.attributeId = attributeId; }


    public void setLevel(int level) { this.level = level; }


    public void setCoefficient(BigDecimal coefficient) { this.coefficient = coefficient; }

    public ResourceAttributeEntity getResourceAttributeEntity() { return resourceAttributeEntity; }

    public void setResourceAttributeEntity(ResourceAttributeEntity resourceAttributeEntity) { this.resourceAttributeEntity = resourceAttributeEntity; }


}



