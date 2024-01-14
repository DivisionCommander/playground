/*
 * SecondaryAttributesEntity.java
 *
 * created at 2023-12-01 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.entities;

import java.util.List;

import bg.sarakt.attributes.Attribute.AttributeType;
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

@Entity
@Table(name = "advanced_attributes")
public class SecondaryAttributesEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "attribute_id", unique = true)
    private long          id;
    @Column(name = "attribute_name")
    private String        name;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private AttributeType type;
    @Column(name = "abbreviation")
    private String        abbr;
    @Column(name = "description")
    private String        description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attributeId")
    private List<AttributeFormulaEntity> formulas;

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public AttributeType getType()
    {
        return type;
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

    public void setType(AttributeType type)
    {
        this.type = type;
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
        sb.append('[').append(id).append(']').append(name).append(" -[").append(abbr).append("]-\t").append(type).append(type);

        return sb.toString();
    }
}
