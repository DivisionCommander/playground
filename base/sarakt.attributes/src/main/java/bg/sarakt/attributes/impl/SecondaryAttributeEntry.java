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
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.SecondaryAttribute;

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