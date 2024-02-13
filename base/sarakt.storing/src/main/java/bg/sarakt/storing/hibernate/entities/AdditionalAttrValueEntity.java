/*
 * AdditionalAttrValueEntity.java
 *
 * created at 2024-01-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity(name = "AdditionalAttributeValues")
@Table(name = "tbl_attribute_values_additional", uniqueConstraints =
    {
            @UniqueConstraint(name = "singleEntry", columnNames =
                {
                        "attribute", "value"
            })
})
public class AdditionalAttrValueEntity implements Serializable {

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202401280322L;

    @Id
    @GeneratedValue
    @Column(name = "pair_id")
    private UUID id;

    @Column(name = "attribute")
    private String attribute;

    @Column(name = "value")
    private BigDecimal value;

    public AdditionalAttrValueEntity() {}

    public AdditionalAttrValueEntity(String attribute, Number value) {
        this.attribute = attribute;
        this.value = new BigDecimal(value.toString());
    }

    public UUID getId() { return id; }

    public String getAttribute() { return attribute; }

    public BigDecimal getValue() { return value; }

    public void setId(UUID id) { this.id = id; }

    public void setAttribute(String attribute) { this.attribute = attribute; }

    public void setValue(BigDecimal value) { this.value = value; }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AdditionalAttrValueEntity [id=" + this.id + ", attribute=" + this.attribute + ", value=" + this.value + "]";
    }
}
