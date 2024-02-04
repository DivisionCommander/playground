/*
 * ResourceAttributeEntry.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.levels.Level;

public final class ResourceAttributeEntry extends AbstractAttributeMapEntry<ResourceAttribute> {

    private final AttributeMapEntry<PrimaryAttribute> primaryAttribute;
    private PrimaryAttributeEntry                     entry2;
    private ExperienceEntry                           experienceEntry;

    ResourceAttributeEntry(ResourceAttribute attribute, IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> attributeMap)
    {
        this(attribute, attributeMap.get(attribute.getPrimaryAttribute()));
        var entry = attributeMap.get(PrimaryAttribute.EXPERIENCE);
        if (entry instanceof ExperienceEntry ee) {
            experienceEntry = ee;
        }
    }
    
    ResourceAttributeEntry(ResourceAttribute attribute, AttributeMapEntry<PrimaryAttribute> primaryAttributeEntry) {
        super(attribute);
        this.primaryAttribute = primaryAttributeEntry;
    }
    /**
    *
    * @deprecated dropping support of {@link Level} and
    *             {@link bg.sarakt.characters.Level} as now
    *             {@link CharacterAttributeMap} would manage leveling of
    *             {@link Attribute}s and their {@link AttributeMapEntry}
    */
    @Deprecated
    ResourceAttributeEntry(ResourceAttribute attribute, AttributeMapEntry<PrimaryAttribute> primaryAttributeEntry, Level level) {
        this(attribute, primaryAttributeEntry);
        setLevel(level);
        recalculate();
    }


    /**
     * {@link PrimaryAttributeEntry#getBaseValue()} multiplied by {@link ResourceAttribute#getCoefficientForLevel(Level)}
     * Need considering general strategy for calculations.
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBaseValue()
     */
    // FIXME ASAP
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
        BigDecimal coefficient = getAttribute().getCoefficientForLevel(experienceEntry.currentLevel());
        BigDecimal primaryAttributeValue = primaryAttribute.getValueForLayer(layer);
        return coefficient.multiply(primaryAttributeValue);
    }

}
