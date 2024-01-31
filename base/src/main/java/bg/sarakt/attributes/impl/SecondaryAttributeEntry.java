/*
 * SecondaryAttributeEntry.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.attributes.levels.Level;

public final class SecondaryAttributeEntry extends AbstractAttributeMapEntry<SecondaryAttribute> implements AttributeMapEntry<SecondaryAttribute>{

    private final IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primaryAttMap;

    SecondaryAttributeEntry(SecondaryAttribute attribute, IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry>  primaryMap) {
        this(attribute, primaryMap, null);
    }

    /**
    *
    * @deprecated dropping support of {@link Level} and
    *             {@link bg.sarakt.characters.Level} as now
    *             {@link CharacterAttributeMap} would manage leveling of
    *             {@link Attribute}s and their {@link AttributeMapEntry}
    */
    @Deprecated(forRemoval =  true, since = "0.0.7")
    SecondaryAttributeEntry(SecondaryAttribute attribute, IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry>  primaryMap, Level level) {
        super(attribute, level);
        primaryAttMap = primaryMap;
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBaseValueForLayer(bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    protected BigDecimal getBaseValueForLayer(ModifierLayer layer) {
        Map<Attribute, Number> values = new HashMap<>();
        for (AttributeMapEntry<PrimaryAttribute> pa : primaryAttMap) {
            BigDecimal valueForLayer = pa.getValueForLayer(layer);
            values.put(pa.getAttribute(), valueForLayer);
        }
        AttributeFormula formula = getAttribute().getFormula(getLevel().getLevelNumber());
        return formula.calculate(values);
    }


    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBaseValue()
     */
    @Override
    public BigDecimal getBaseValue() { return getBaseValueForLayer(ModifierLayer.getLowestLayer()); }

}