/*
 * ResourceAttributeEntry.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;

import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.characters.Level;

public final class ResourceAttributeEntry extends AbstractAttributeMapEntry<ResourceAttribute> {

    private final AttributeMapEntry<PrimaryAttribute> primaryAttribute;

    ResourceAttributeEntry(ResourceAttribute attribute, AttributeMapEntry<PrimaryAttribute> primaryAttributeEntry, Level level) {
        super(attribute, level);
        primaryAttribute = primaryAttributeEntry;
    }


    /**
     * {@link PrimaryAttributeEntry#getBaseValue()} multiplied by {@link ResourceAttribute#getCoefficientForLevel(Level)}
     * Need considering general strategy for calculations.
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBaseValue()
     */
    @Override
    public BigDecimal getBaseValue() {
        BigDecimal primaryAttributeValue = primaryAttribute.getBaseValue();
        BigDecimal coefficient = getAttribute().getCoefficientForLevel(getLevel());
        BigDecimal baseValue = coefficient.multiply(primaryAttributeValue);
        return baseValue;
    }


    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBaseValueForLayer(bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    protected BigDecimal getBaseValueForLayer(ModifierLayer layer) {
        BigDecimal coefficient = getAttribute().getCoefficientForLevel(getLevel());
        BigDecimal primaryAttributeValue = primaryAttribute.getValueForLayer(layer);
        return coefficient.multiply(primaryAttributeValue);
    }

}
