/*
 * ClassFactory.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.base.utils.factories;

import static bg.sarakt.characters.attributes1.Attributes.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import bg.sarakt.attributes.Attribute;

@Deprecated(forRemoval = true, since="0.0.7")
public final class UnitClassFactory
{

    private static final UnitClassFactory INSTANCE = new UnitClassFactory();

    private final NavigableMap<Integer, Map<Attribute, Double>> advancedAttribute = TEMP();

    private UnitClassFactory()
    {
        TEMP();
    }

    public static NavigableMap<Integer, Map<Attribute, Double>> getTempAttributes()
    {
        return INSTANCE.advancedAttribute;

    }

    private NavigableMap<Integer, Map<Attribute, Double>> TEMP() {
        NavigableMap<Integer, Map<Attribute, Double>> advancedAttribute = new TreeMap<>();
        Map<Attribute, Double> map = new HashMap<>();
//        map.put(HIT_POINTS, 5.5);
//        map.put(ATTACK_SPEED, 1.0);
//        map.put(CAST_RATE, 1.0);
//        advancedAttribute.put(1, map);
//        map = new HashMap<>();
//        map.put(HIT_POINTS, 6.0);
//        map.put(ATTACK_SPEED, 0.9);
//        map.put(CAST_RATE, 0.9);
//        advancedAttribute.put(10, map);

        return Collections.unmodifiableNavigableMap(advancedAttribute);
    }
    
}
