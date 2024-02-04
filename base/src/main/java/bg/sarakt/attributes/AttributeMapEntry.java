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

import bg.sarakt.attributes.levels.Level;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.base.utils.ForRemoval;

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

    @Deprecated(forRemoval = true, since = "0.0.6")
    void levelUp();

    T getAttribute();

    BigDecimal getBaseValue();

    BigDecimal getValueForLayer(ModifierLayer layer);

    /**
     * Get value for the highest {@link ModifierLayer}.
     *
     * @return
     */
    BigDecimal getCurrentValue();
    
    @Dummy(since = "0.0.11", to = "UNKNOWN", description = "Workarround until finally get remove Level from the enry")
    @ForRemoval(expectedRemovalVersion = "UNKNOWN")
    AttributeMapEntry<T> setLevel(Level level);
}
