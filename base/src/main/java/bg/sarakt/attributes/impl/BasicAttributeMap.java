/*
 * BasicAttributeMap.java
 *
 * created at 2024-01-14 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;

public abstract class BasicAttributeMap<T extends Attribute> extends AbstractAttributeMap<T> {

    private final Map<T, Integer> calculatedValues;

    protected BasicAttributeMap(Map<T, Integer> attributes) {
        super(attributes);
        calculatedValues = new HashMap<>();
    }

    /**
     *
     * @see bg.sarakt.attributes.impl.AbstractAttributeMap#addModifier(bg.sarakt.attributes.AttributeModifier)
     */
    @Override
    public boolean addModifier(AttributeModifier<T> modifier) {
        // super.addModifier0(modifier);
        return super.addModifier(modifier);
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMap#removeModifier(bg.sarakt.attributes.AttributeModifier)
     */
    @Override
    public boolean removeModifier(AttributeModifier<T> modifier) {
        // super.removeModifier0(modifier);
        return super.removeModifier(modifier);
    }

    @Override
    public Map<T, Integer> getAllAttributes() { return Collections.unmodifiableMap(calculatedValues); }
}
