/*
 * ModifiableAttributeMap.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.util.Collection;

public interface ModifiableAttributeMap<A extends Attribute, E extends AttributeMapEntry<A>> extends IterableAttributeMap<A, E>{

    E get(A attribute);

    void addModifier(AttributeModifier<A> modifier);

    void addModifiers(Collection<AttributeModifier<A>> modifiers);

    void removeModifier(AttributeModifier<A> modifier);

    void removeModifiers(Collection<AttributeModifier<A>> modifiers);

}
