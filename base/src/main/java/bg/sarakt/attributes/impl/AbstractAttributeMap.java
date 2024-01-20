/*
 * AbstractAttributeMap.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import bg.sarakt.attributes.ModifiableAttributeMap;
import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.characters.Level;

public abstract class AbstractAttributeMap<A extends Attribute, I extends AttributeMapEntry<A>> implements ModifiableAttributeMap<A, I> {

    protected Level level;

    protected AbstractAttributeMap(Level level) {
        this.level = level;
    }

    protected abstract AttributeMapEntry<A> getEntry(A attr);

    protected abstract void changeModifiers(Collection<AttributeModifier<A>> modifiers, boolean add);

    @Override
    public abstract Map<A, BigDecimal> getAllValues();

    @Override
    public abstract void levelUp(Level level);

    @Override
    public void addModifier(AttributeModifier<A> modifier) {
        getEntry(modifier.getAttribute()).addModifier(modifier);
    }

    @Override
    public void addModifiers(Collection<AttributeModifier<A>> modifiers) {
        changeModifiers(modifiers, false);
    }

    @Override
    public void removeModifier(AttributeModifier<A> modifier) {
        getEntry(modifier.getAttribute()).removeModifier(modifier);
    }

    @Override
    public void removeModifiers(Collection<AttributeModifier<A>> modifiers) {
        changeModifiers(modifiers, false);
    }

    /**
     * @see bg.sarakt.attributes.ModifiableAttributeMap#getAttributeValueForLayer(bg.sarakt.attributes.Attribute,
     *      bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    public BigDecimal getAttributeValueForLayer(A attribute, ModifierLayer layer) {
        return getEntry(attribute).getValueForLayer(layer);
    }

    @Override
    public BigDecimal getCurrentAttributeValue(A attribute) {
        return getEntry(attribute).getCurrentValue();
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public abstract Iterator<I> iterator() ;
}
