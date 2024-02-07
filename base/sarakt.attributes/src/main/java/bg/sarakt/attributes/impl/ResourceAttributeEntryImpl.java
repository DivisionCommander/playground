/*
 * ResourceAttributeEntryImpl.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.math.MathContext;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.base.utils.ForRemoval;


public final class ResourceAttributeEntryImpl extends AbstractAttributeMapEntry<ResourceAttribute> implements ResourceAttributeEntry {

    private final AttributeMapEntry<PrimaryAttribute> primaryAttribute;
    private ExperienceEntry                           experienceEntry;
    private BigDecimal                                currentValue = null;
    private BigDecimal                                maxValue     = null;
    
    ResourceAttributeEntryImpl(ResourceAttribute attribute, IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> attributeMap)
    {
        super(attribute);
        this.primaryAttribute = attributeMap.get(attribute.getPrimaryAttribute());
        var entry = attributeMap.get(PrimaryAttribute.EXPERIENCE);
        if (entry instanceof ExperienceEntry ee) {
            experienceEntry = ee;
        }
        recalculate();
    }
    


    /**
     * {@link PrimaryAttributeEntry#getBaseValue()} multiplied by {@link ResourceAttribute#getCoefficientForLevel(Level)}
     * Need considering general strategy for calculations.
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBaseValue()
     */
    @Override
    public BigDecimal getBaseValue() {
        BigDecimal primaryAttributeValue = primaryAttribute.getBaseValue();
        BigDecimal coefficient = getAttribute().getCoefficientForLevel(experienceEntry.getLevelNumber());
        return coefficient.multiply(primaryAttributeValue);
    }


    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBaseValueForLayer(bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    protected BigDecimal getBaseValueForLayer(ModifierLayer layer) {
        BigDecimal coefficient = getAttribute().getCoefficientForLevel(experienceEntry.getLevelNumber());
        BigDecimal primaryAttributeValue = primaryAttribute.getValueForLayer(layer);
        return coefficient.multiply(primaryAttributeValue);
    }
    
    /**
     * @return Returns value of currentValue.
     */
    @Override
    public BigDecimal getCurrentValue() {
        if (this.currentValue == null) {
            this.currentValue = currentValue();
        }
        return this.currentValue;
    }
    
    /**
     * @see bg.sarakt.attributes.impl.ResourceAttributeEntry#getMaxValue()
     */
    @Override
    public BigDecimal getMaxValue() {
        if (this.maxValue == null) {
            this.maxValue = super.getCurrentValue();
        }
        return this.maxValue;
    }
    
    /**
     * @see bg.sarakt.attributes.impl.ResourceAttributeEntry#getMaxValueForLayer(bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    public BigDecimal getMaxValueForLayer(ModifierLayer layer) {
        return super.getValueForLayer(layer);
    }
    
    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getValueForLayer(bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    public BigDecimal getValueForLayer(ModifierLayer layer) {
        if (layer.equals(ModifierLayer.getHighestLayer())) {
            return getMaxValue();
        }
        BigDecimal current = getCurrentValue();
        BigDecimal max = getMaxValue();
        BigDecimal perLayer = super.getValueForLayer(layer);
        if (current.compareTo(max) == 0) {
            return super.getValueForLayer(layer);
        }
        
        return current.divide(max, MathContext.DECIMAL32).multiply(perLayer);
    }
    
    /**
     * @see bg.sarakt.attributes.impl.ResourceAttributeEntry#consume(java.math.BigDecimal)
     */
    @Override
    public void consume(BigDecimal amount) {
        BigDecimal current = getCurrentValue();
        this.currentValue = current.subtract(amount);
    }
    
    /**
     * @see bg.sarakt.attributes.impl.ResourceAttributeEntry#deplete()
     */
    @Override
    public void deplete() {
        this.currentValue = BigDecimal.ZERO;
    }
    
    /**
     * @see bg.sarakt.attributes.impl.ResourceAttributeEntry#restore(java.math.BigDecimal)
     */
    @Override
    public void restore(BigDecimal amount) {
        BigDecimal newValue = currentValue.add(amount);
        this.currentValue = newValue.compareTo(maxValue) < 0 ? newValue : maxValue;
    }
    
    /**
     * @see bg.sarakt.attributes.impl.ResourceAttributeEntry#restore()
     */
    @Override
    public void restore() {
        this.currentValue = maxValue;
    }
    
    @Override
    public String toString() {
        return "ResourceAttributeEntryImpl [primaryAttribute="
               + primaryAttribute
               + ", experienceEntry="
               + experienceEntry
               + ", maxValue="
               + maxValue
               + ", getCurrentValue()="
               + getCurrentValue()
               + ", getMaxValue()="
               + getMaxValue()
               + "]";
    }
    
    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#recalculate()
     */
    @Override
    public void recalculate() {
        super.recalculate();
        maxValue = super.currentValue();
        currentValue = super.currentValue();
    }
}