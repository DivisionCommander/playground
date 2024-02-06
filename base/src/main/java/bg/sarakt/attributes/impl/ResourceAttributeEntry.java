/*
 * ResourceAttributeEntry.java
 *
 * created at 2024-02-05 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;

import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ResourceAttribute;

/**
 * An interface to back-up already existing {@link ResourceAttributeEntryImpl}
 * 
 * @since 0.0.13
 */
public interface ResourceAttributeEntry extends AttributeMapEntry<ResourceAttribute> {
    
    /**
     * Consume provided amount of the resource
     * 
     * @param amount
     */
    void consume(BigDecimal amount);
    
    /**
     * Consume all of this resource effectively turns
     * {@link ResourceAttributeEntry#getCurrentValue()} to <code>0</code>.
     */
    void deplete();
    
    /**
     * Restore some amount of this {@link ResourceAttribute}.
     * 
     * @param amount
     */
    void restore(BigDecimal amount);
    
    /**
     * Completely restore this {@link ResourceAttribute} to its
     * {@link ResourceAttributeEntry#getMaxValue()}
     */
    void restore();
    
    /**
     * Get the current value of the Resource such it is after consuming or restoring
     * some of it. Usually cannot exceed the
     * {@link ResourceAttributeEntry#getMaxValue()}
     * 
     * @see bg.sarakt.attributes.AttributeMapEntry#getCurrentValue()
     */
    @Override
    BigDecimal getCurrentValue();
    
    /**
     * Get the current value of the Resource for the specified {@link ModifierLayer}
     * such it is after consuming or restoring some of it. Usually cannot exceed the
     * {@link ResourceAttributeEntry#getMaxValueForLayer(ModifierLayer)}.
     * 
     * @see bg.sarakt.attributes.AttributeMapEntry#getCurrentValue()
     */
    
    @Override
    BigDecimal getValueForLayer(ModifierLayer layer);
    
    /**
     * Get the maximal value of the resource as it is formed by
     * {@link ResourceAttribute}'s base value and all applied
     * {@link AttributeModifier}s.
     * 
     * @return
     */
    BigDecimal getMaxValue();
    
    /**
     * Get the maximal value of the resource specified {@link ModifierLayer} as it
     * is formed by {@link ResourceAttribute}'s base value and all applied
     * {@link AttributeModifier}s.
     * 
     * @return
     */
    BigDecimal getMaxValueForLayer(ModifierLayer layer);
}
