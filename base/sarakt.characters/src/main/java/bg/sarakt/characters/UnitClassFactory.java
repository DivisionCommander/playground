/*
 * ClassFactory.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters;


import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.base.utils.ForRemoval;

/**
 * @deprecated
 */
@Deprecated(forRemoval = true)
@ForRemoval(expectedRemovalVersion = "0.0.5")
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

        return Collections.unmodifiableNavigableMap(new TreeMap<>());
    }
    
}
