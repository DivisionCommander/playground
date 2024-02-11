/*
 * ResourceAttributeEntryImpl.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.resources.impl;

import java.math.BigDecimal;
import java.math.MathContext;

import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.internal.AbstractAttributeMapEntry;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.primary.ExperienceEntryImpl;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.primary.PrimaryAttributeEntry;
import bg.sarakt.attributes.resources.ResourceAttribute;
import bg.sarakt.attributes.resources.ResourceAttributeEntry;


public final class ResourceAttributeEntryImpl extends AbstractAttributeMapEntry<ResourceAttribute> implements ResourceAttributeEntry {

    private final AttributeMapEntry<PrimaryAttribute> primaryAttribute;
    private ExperienceEntryImpl                           experienceEntry;
    private BigDecimal                                currentValue = null;
    private BigDecimal                                maxValue     = null;
    
    ResourceAttributeEntryImpl(ResourceAttribute attribute, Iterable<PrimaryAttributeEntry> attributeMap) {
        super(attribute);
        PrimaryAttribute pa = attribute.getPrimaryAttribute();
        AttributeMapEntry<PrimaryAttribute> pae = null;
        ExperienceEntryImpl exp = null;
        for (var entry : attributeMap) {
            if (pae == null && entry.getAttribute() == pa) {
                pae = entry;
            }
            if (exp == null && entry.getAttribute() == PrimaryAttribute.EXPERIENCE && entry instanceof ExperienceEntryImpl ee) {
                exp = ee;
            }
            if (pae != null && exp != null) {
                break;
            }
        }
        this.primaryAttribute = pae;
        this.experienceEntry = exp;
        recalculate();
    }
    


    /**
     * {@link PrimaryAttributeEntry#getBaseValue()} multiplied by {@link ResourceAttribute#getCoefficientForLevel(Level)}
     * Need considering general strategy for calculations.
     * @see bg.sarakt.attributes.internal.AbstractAttributeMapEntry#getBaseValue()
     */
    @Override
    public BigDecimal getBaseValue() {
        BigDecimal primaryAttributeValue = primaryAttribute.getBaseValue();
        BigDecimal coefficient = getAttribute().getCoefficientForLevel(experienceEntry.getLevelNumber());
        return coefficient.multiply(primaryAttributeValue);
    }


    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeMapEntry#getBaseValueForLayer(bg.sarakt.attributes.ModifierLayer)
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
     * @see bg.sarakt.attributes.resources.ResourceAttributeEntry#getMaxValue()
     */
    @Override
    public BigDecimal getMaxValue() {
        if (this.maxValue == null) {
            this.maxValue = super.getCurrentValue();
        }
        return this.maxValue;
    }
    
    /**
     * @see bg.sarakt.attributes.resources.ResourceAttributeEntry#getMaxValueForLayer(bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    public BigDecimal getMaxValueForLayer(ModifierLayer layer) {
        return super.getValueForLayer(layer);
    }
    
    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeMapEntry#getValueForLayer(bg.sarakt.attributes.ModifierLayer)
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
     * @see bg.sarakt.attributes.resources.ResourceAttributeEntry#consume(java.math.BigDecimal)
     */
    @Override
    public void consume(BigDecimal amount) {
        BigDecimal current = getCurrentValue();
        this.currentValue = current.subtract(amount);
    }
    
    /**
     * @see bg.sarakt.attributes.resources.ResourceAttributeEntry#deplete()
     */
    @Override
    public void deplete() {
        this.currentValue = BigDecimal.ZERO;
    }
    
    /**
     * @see bg.sarakt.attributes.resources.ResourceAttributeEntry#restore(java.math.BigDecimal)
     */
    @Override
    public void restore(BigDecimal amount) {
        BigDecimal newValue = currentValue.add(amount);
        this.currentValue = newValue.compareTo(maxValue) < 0 ? newValue : maxValue;
    }
    
    /**
     * @see bg.sarakt.attributes.resources.ResourceAttributeEntry#restore()
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
     * @see bg.sarakt.attributes.internal.AbstractAttributeMapEntry#recalculate()
     */
    @Override
    public void recalculate() {
        super.recalculate();
        maxValue = super.currentValue();
        currentValue = super.currentValue();
    }
}