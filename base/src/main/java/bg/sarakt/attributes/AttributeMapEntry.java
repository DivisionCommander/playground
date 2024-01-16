/*
 * AttributeMapEntry.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.math.BigDecimal;
import java.util.Collection;

public interface AttributeMapEntry<T extends Attribute> {

    /**
     * Add {@link AttributeModifier} to the current entry.
     *
     * @param modifier
     */
    void addModifier(AttributeModifier<T> modifier);

    /**
     * Add multiple {@link AttributeModifier}s to the current entry.
     *
     * @param modifiers
     */
    void addModifiers(Collection<AttributeModifier<T>> modifiers);

    void removeModifier(AttributeModifier<T> modifier);

    void removeModifiers(Collection<AttributeModifier<T>> modifiers);

    void replaceModifier(AttributeModifier<T> old, AttributeModifier<T> newM);

    void levelUp();

    T getAttribute();

    BigDecimal getBasicValue();

    BigDecimal getValueForLayer(ModifierLayer layer);

    /**
     * Get value for the highest {@link ModifierLayer}.
     *
     * @return
     */
    BigDecimal getCurrentValue();
}
