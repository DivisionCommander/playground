/*
 * ResourceAttributeEntity.java
 *
 * created at 2024-01-21 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.entities;

import java.io.Serializable;
import java.util.List;


import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name ="attribute")
@Table(name = "tbl_attributes_resource")
public class ResourceAttributeEntity implements Serializable {

    public static final Class<?> ENTRY = ResourceAttribute.class;

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 20240121156L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "attribute_id", unique = true)
    private long             id;
    @Column(name = "name", unique = true)
    private String           name;
    @Column(name = "attribute_group")
    @Enumerated(EnumType.STRING)
    private AttributeGroup   group;
    @Column(name = "primary_attribute")
    @Enumerated(EnumType.STRING)
    private PrimaryAttribute primeAttribute;
    @Column(name = "abbreviation", unique = true)
    private String           abbr;
    @Column(name = "description")
    private String           descrption;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attributeId")
    private List<ResourceAttributeCoefficientEntity> coefficients;

    public ResourceAttributeEntity() {}

    public ResourceAttributeEntity(String name, AttributeGroup group, PrimaryAttribute primary, String abbr, String descrption) {
        super();
        this.name = name;
        this.group = group;
        this.primeAttribute = primary;
        this.abbr = abbr;
        this.descrption = descrption;
    }

    public long getId() { return id; }

    public String getName() { return name; }

    public AttributeGroup getGroup() { return group; }

    public PrimaryAttribute getPrimaryAttribute() { return primeAttribute; }

    public String getAbbr() { return abbr; }

    public String getDescrption() { return descrption; }

    public List<ResourceAttributeCoefficientEntity> getCoefficients() { return coefficients; }

    public void setId(long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setGroup(AttributeGroup group) { this.group = group; }

    public void setPrimaryAttribute(PrimaryAttribute primaryAttribute) { this.primeAttribute = primaryAttribute; }

    public void setAbbr(String abbr) { this.abbr = abbr; }

    public void setDescrption(String descrption) { this.descrption = descrption; }

    public void setCoefficients(List<ResourceAttributeCoefficientEntity> coefficients) { this.coefficients = coefficients; }
}