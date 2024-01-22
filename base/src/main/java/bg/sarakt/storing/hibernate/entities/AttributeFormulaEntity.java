/*
 * SecondaryAttributeFormulaEntity.java
 *
 * created at 2023-12-01 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_attribute_formulas")
public class AttributeFormulaEntity implements Serializable
{

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 2483025876507102758L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "formula_id")
    private long   formulaId;
    @Column(name = "attribute_id", insertable = false, updatable = false)
    private long   attributeId;
    @Column(name = "level")
    private int    level;
    @Column(name = "formula")
    @Lob
    private byte[] formula;
    @Column(name = "asString")
    private String asString;

    @ManyToOne
    @JoinColumn(name = "attribute_id", nullable = false)
    private SecondaryAttributeEntity advancedAttributesEntity;

    public long getFormulaId()
    {
        return formulaId;
    }

    public long getAttributeId()
    {
        return attributeId;
    }

    public int getLevel()
    {
        return level;
    }

    public byte[] getFormula()
    {
        return formula;
    }

    public SecondaryAttributeEntity getAdvancedAttributesEntity()
    {
        return advancedAttributesEntity;
    }

    public void setFormulaId(long formulaId)
    {
        this.formulaId = formulaId;
    }

    public void setAttributeId(long attributeId)
    {
        this.attributeId = attributeId;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public void setFormula(byte[] formula)
    {
        this.formula = formula;
    }

    public void setAdvancedAttributesEntity(SecondaryAttributeEntity aae)
    {
        this.advancedAttributesEntity = aae;
    }


    public String getAsString()
    {
        return asString;
    }


    public void setAsString(String asString)
    {
        this.asString = asString;
    }
}
