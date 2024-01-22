/*
 * IterableAttributeMap.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.util.Iterator;

public interface IterableAttributeMap<A extends Attribute, E extends AttributeMapEntry<A>> extends Iterable<E>, AttributeMap<A> {

    @Override
    Iterator<E> iterator();

}
