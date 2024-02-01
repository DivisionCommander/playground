/*
 * SecondaryAttributesEntity.java
 *
 * created at 2023-12-01 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.entities;

import java.io.Serializable;
import java.util.List;

import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.SecondaryAttribute;
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
import jakarta.persistence.Transient;

@Entity
@Table(name = "tbl_attributes_secondary")
public class SecondaryAttributeEntity implements Serializable
{
    @Transient()
    public static final Class<?> ENTRY = SecondaryAttribute.class;

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202401201803L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "attribute_id", unique = true)
    private long          id;
    @Column(name = "attribute_name", unique = true)
    private String        name;
    @Column(name = "attribute_group")
    @Enumerated(EnumType.STRING)
    private AttributeGroup group;
    @Column(name = "abbreviation", unique = true)
    private String        abbr;
    @Column(name = "description")
    private String        description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "attributeId")
    private List<AttributeFormulaEntity> formulas;

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public AttributeGroup getGroup()
    {
        return group;
    }

    public String getAbbr()
    {
        return abbr;
    }

    public String getDescription()
    {
        return description;
    }

    public List<AttributeFormulaEntity> getFormulas()
    {
        return formulas;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setGroup(AttributeGroup type)
    {
        this.group = type;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setFormulas(List<AttributeFormulaEntity> formulas)
    {
        this.formulas = formulas;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(id).append(']').append(name).append(" -[").append(abbr).append("]-\t").append("group[").append(group).append("]");

        return sb.toString();
    }
}
