/*
 * DynamicUnitClass.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1.impls;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.base.utils.factories.UnitClassFactory;
import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.characters.attributes1.UnitClass;

public class DynamicUnitClass implements UnitClass
{

    private final Map<PrimaryAttribute, Integer>                 basicAttributes;
    private final Map<PrimaryAttribute, Double>                  basicAttributesCoefficients;
    private final NavigableMap<Integer, Map<Attribute, Double>> advancedAttribute;
    private final String                                        className;

    public DynamicUnitClass(String name)
    {
        this.className = name;
        basicAttributes = new EnumMap<>(PrimaryAttribute.class);
        basicAttributesCoefficients = new EnumMap<>(PrimaryAttribute.class);
        EnumSet.allOf(PrimaryAttribute.class).stream().forEach(ba -> {
            basicAttributes.put(ba, 9);
            basicAttributesCoefficients.put(ba, 1.0);
        });

        advancedAttribute = new TreeMap<>();
        TEMP();
    }

    // FIXME
    private void TEMP()
    {
        NavigableMap<Integer, Map<Attribute, Double>> adv = UnitClassFactory.getTempAttributes();
        advancedAttribute.putAll(adv);
    }

    @Override
    public String className()
    {
        return className;
    }

    /**
     * @see bg.sarakt.characters.attributes1.UnitClass#getAttributesForLevel(int)
     */
    @Override
    public Set<AttributeValuePair> getAttributesForLevel(int newLevel)
    {
        Set<AttributeValuePair> attributes = new HashSet<>();
        for (PrimaryAttribute ba : PrimaryAttribute.getAllPrimaryAttributes())
        {
            Integer value = basicAttributes.get(ba);
            Double coef = basicAttributesCoefficients.get(ba) * newLevel;
            coef += value;
            AttributeValuePair pair = new AttributeValuePair(ba, coef.intValue());
            attributes.add(pair);
        }
        Map<Attribute, Double> advanced = advancedAttribute.get(advancedAttribute.floorKey(newLevel));
        advanced.entrySet().stream().map(e -> formatPair(e, newLevel)).forEach(attributes::add);
        return attributes;
    }

    private <T extends Attribute> AttributeValuePair formatPair(Entry<T, Double> entry, int level)
    {
        Double value = entry.getValue() * level;
        return new AttributeValuePair(entry.getKey(), value.intValue());
    }
}
