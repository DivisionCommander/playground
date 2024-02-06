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
import bg.sarakt.base.utils.ForRemoval;

import org.springframework.lang.NonNull;

public final class SecondaryAttributeEntry extends AbstractAttributeMapEntry<SecondaryAttribute> implements AttributeMapEntry<SecondaryAttribute>{

    private final IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primaryAttMap;
    private ExperienceEntry                                                     experienceEntry;

    SecondaryAttributeEntry(@NonNull SecondaryAttribute attribute, IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primaryMap) {
        super(attribute);
        this.primaryAttMap = primaryMap;
        PrimaryAttributeEntry experience = primaryMap.get(PrimaryAttribute.EXPERIENCE);
        if (experience instanceof ExperienceEntry ee) {
            this.experienceEntry = ee;
        }
    }

    /**
    *
    * @deprecated dropping support of {@link Level} and
    *             {@link bg.sarakt.characters.Level} as now
    *             {@link CharacterAttributeMap} would manage leveling of
    *             {@link Attribute}s and their {@link AttributeMapEntry}
    */
    @Deprecated(forRemoval =  true, since = "0.0.7")
    @ForRemoval(since = "0.0.7", expectedRemovalVersion = "0.0.15")
    SecondaryAttributeEntry(SecondaryAttribute attribute, IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry>  primaryMap, Level level) {
        this(attribute, primaryMap);
        setLevel(level);
        recalculate();
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
        AttributeFormula formula = getAttribute().getFormula(experienceEntry.getLevelNumber());
        return formula.calculate(values);
    }


    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBaseValue()
     */
    @Override
    public BigDecimal getBaseValue() { return getBaseValueForLayer(ModifierLayer.getLowestLayer()); }

}