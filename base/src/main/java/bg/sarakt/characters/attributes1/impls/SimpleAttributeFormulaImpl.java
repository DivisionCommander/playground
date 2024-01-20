/*
 * SimpleAttributeFormulaImpl.java
 *
 * created at 2023-12-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1.impls;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.impl.PrimaryAttribute;

public class SimpleAttributeFormulaImpl implements AttributeFormula
{

    private String                         name;
    @JsonSerialize
    @JsonDeserialize
    private Map<PrimaryAttribute, Double> attributes;

    public SimpleAttributeFormulaImpl()
    {
        this("dummy");
    }

    public SimpleAttributeFormulaImpl(String name)
    {
        this(name, null);
    }

    public SimpleAttributeFormulaImpl(String name, Map<PrimaryAttribute, Double> attr)
    {
        this.name = name;
        attributes = (attr == null) ? new EnumMap<>(PrimaryAttribute.class) : new HashMap<>(attr);
    }

    public void addAttributeFormula(PrimaryAttribute pa, double value)
    {
        double oldValue = attributes.getOrDefault(pa, 0.0);
        attributes.put(pa, (oldValue + value));
    }

    public void addAttributeFormulas(PrimaryAttribute pa, double... values)
    {
        double value = 0.0;

        for (double v : values)
        {
            value += v;
        }
        addAttributeFormula(pa, value);
    }

    /**
     * @return Returns value of name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @see bg.sarakt.characters.attributes1.AttributeFormula#calculate(java.util.Map)
     */
    @Override
    public BigDecimal calculate(Map<Attribute, Number> values)
    {
        Double value = 0.0;

        for (Entry<Attribute, Number> entry : values.entrySet())
        {
            Double coefficient = attributes.getOrDefault(entry.getKey(), 0.0);
            value += coefficient * entry.getValue().doubleValue();

        }
//        return value;
        return BigDecimal.valueOf(value);

    }

}
