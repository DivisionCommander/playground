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
import java.util.UUID;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.base.utils.ForRemoval;
import bg.sarakt.characters.UnitClassFactory;
import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.characters.attributes1.UnitClass;

@Dummy
@ForRemoval(expectedRemovalVersion = ForRemoval.UNKNOWN_VERSION)
public class DynamicUnitClass implements UnitClass
{

    /** field <code>serialVersionUID</code> */
    private static final long                                   serialVersionUID = 1L;
    private final UUID uuid  =UUID.randomUUID();
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
        temp();
    }
/**
 * @see bg.sarakt.characters.attributes1.UnitClass#unitClassId()
 */
@Override
public String unitClassId() {
    return uuid.toString();
}

/**
 * @deprecated
 */
@Deprecated(since = "0.0.3", forRemoval = true)
@ForRemoval(since = "0.0.3", expectedRemovalVersion = "0.0.5")
    private void temp()
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
     * 
     * @deprecated
     */
    @Override
    @Deprecated(since = "0.0.3", forRemoval = true)
    @ForRemoval(since = "0.0.3", expectedRemovalVersion = "0.0.5")
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
