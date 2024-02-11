/*
 * SecondaryAttributeEntry.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.secondary.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.internal.AbstractAttributeMapEntry;
import bg.sarakt.attributes.primary.ExperienceEntryImpl;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.primary.PrimaryAttributeEntry;
import bg.sarakt.attributes.secondary.SecondaryAttribute;
import bg.sarakt.attributes.secondary.SecondaryAttributeEntry;

import org.springframework.lang.NonNull;

public final class SecondaryAttributeEntryImpl extends AbstractAttributeMapEntry<SecondaryAttribute> implements SecondaryAttributeEntry {

    private final Iterable<PrimaryAttributeEntry> primaryAttMap;
    private ExperienceEntryImpl                                                     experienceEntry;

    public SecondaryAttributeEntryImpl(@NonNull SecondaryAttribute attribute, Iterable<PrimaryAttributeEntry> primaryMap) {
        super(attribute);
        this.primaryAttMap = primaryMap;
        for (PrimaryAttributeEntry e : primaryMap) {
            if (e.getAttribute() == PrimaryAttribute.EXPERIENCE && (e instanceof ExperienceEntryImpl ee)) {
                this.experienceEntry = ee;
                break;
            }
        }
        recalculate();
    }

    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeMapEntry#getBaseValueForLayer(bg.sarakt.attributes.ModifierLayer)
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
     * @see bg.sarakt.attributes.internal.AbstractAttributeMapEntry#getBaseValue()
     */
    @Override
    public BigDecimal getBaseValue() { return getBaseValueForLayer(ModifierLayer.getLowestLayer()); }

}