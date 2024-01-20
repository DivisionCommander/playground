/*
 * SecondaryAttributeImpl.java
 *
 * created at 2023-11-29 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;

import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.base.Pair;
import bg.sarakt.base.Pair.PairImpl;
import bg.sarakt.base.utils.FormulaSerializer;
import bg.sarakt.storing.hibernate.entities.AttributeFormulaEntity;
import bg.sarakt.storing.hibernate.entities.SecondaryAttributesEntity;

public class SecondaryAttributeImpl implements SecondaryAttribute
{

    private long id;

    private final String        fullName;
    private final String        abbreviation;
    private final AttributeGroup type;
    private final String        description;

    private final NavigableMap<Integer, AttributeFormula> formulas = new TreeMap<>();

    public SecondaryAttributeImpl(long id, String fullName, String abbreviation, AttributeGroup type, String description)
    {
        super();
        this.id = id;
        this.fullName = fullName;
        this.abbreviation = abbreviation;
        this.type = type;
        this.description = description;
    }

    public SecondaryAttributeImpl(String fullName, String abbreviation, AttributeGroup type, String description)
    {
        this(System.currentTimeMillis(), fullName, abbreviation, type, description);
    }

    /**
     * @see bg.sarakt.attributes.Attribute#fullName()
     */
    @Override
    public String fullName()
    {
        return fullName;
    }

    /**
     * @see bg.sarakt.attributes.Attribute#abbreviation()
     */
    @Override
    public String abbreviation()
    {
        return abbreviation;
    }

    @Override
    public AttributeGroup group()
    {
        return this.type;
    }

    @Override
    public String description()
    {
        return this.description;
    }

    public void addFormula(int level, AttributeFormula formula)
    {
        formulas.put(level, formula);
    }

    /**
     * @see bg.sarakt.attributes.SecondaryAttribute#getFormula()
     */
    @Override
    public AttributeFormula getFormula(int level)
    {
        return formulas.floorEntry(level).getValue();
    }

    public List<Pair<Integer, AttributeFormula>> getAllFormulas()
    {
        return formulas.entrySet().stream().map(this::formula).collect(Collectors.toList());

    }

    private Pair<Integer, AttributeFormula> formula(Entry<Integer, AttributeFormula> e)
    {
        return new PairImpl<Integer, AttributeFormula>(e.getKey(), e.getValue());
    }

    /**
     * @see bg.sarakt.attributes.SecondaryAttribute#getId()
     */
    @Override
    public long getId()
    {
        return id;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(id).append(']').append(fullName).append(" -[").append(abbreviation).append("]-\t").append(type).append(type.ordinal());

        return sb.toString();
    }

    public SecondaryAttributesEntity toEntity()
    {

        SecondaryAttributesEntity aae = new SecondaryAttributesEntity();
        aae.setName(this.fullName);
        aae.setAbbr(this.abbreviation);
        aae.setGroup(this.type);
        List<AttributeFormulaEntity> preparedFormulas = this.formulas.entrySet().stream().map(this::convert).collect(Collectors.toList());
        aae.setDescription(this.description);
        aae.setFormulas(preparedFormulas);

        return aae;
    }

    private AttributeFormulaEntity convert(Entry<Integer, AttributeFormula> formulaPerLevel)
    {
        int level = formulaPerLevel.getKey();
        AttributeFormula formula = formulaPerLevel.getValue();
        byte[] byteFormula = null;
        try
        {
            byteFormula = FormulaSerializer.getInstance().writeFormula(formula);
        }
        catch (JsonProcessingException jpse)
        {
            System.err.println("FIXME:LOGG AND process error!");
        }

        AttributeFormulaEntity aafe = new AttributeFormulaEntity();
        aafe.setAttributeId(id);
        aafe.setLevel(level);
        aafe.setFormula(byteFormula);
        return aafe;
    }

}