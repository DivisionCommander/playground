/*
 * ModifiableAttributeMap.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.util.Collection;

import bg.sarakt.attributes.levels.Level;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.base.utils.ForRemoval;

public interface ModifiableAttributeMap<A extends Attribute, E extends AttributeMapEntry<A>> extends IterableAttributeMap<A, E>{

    E get(A attribute);

    void addModifier(AttributeModifier<A> modifier);

    void addModifiers(Collection<AttributeModifier<A>> modifiers);

    void removeModifier(AttributeModifier<A> modifier);

    void removeModifiers(Collection<AttributeModifier<A>> modifiers);

    // void recalculate();
    
    @Dummy(since = "0.0.11", to = "UNKNOWN", description = "Workarround until finally get remove Level from the enry")
    @ForRemoval(expectedRemovalVersion = "UNKNOWN")
    ModifiableAttributeMap<A, E> setLevel(Level level);
}

