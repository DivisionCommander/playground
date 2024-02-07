/*
 * IterableAttributeMap.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface IterableAttributeMap<A extends Attribute, E extends AttributeMapEntry<A>> extends Iterable<E>, AttributeMap<A> {

    /**
     * Get the entry of the provided {@link Attribute}. Since the operation can be
     * achieved via {@link Iterator}, this method should be here instead of
     * {@link ModifiableAttributeMap}
     * 
     * @since 0.0.12 pull up from {@link ModifiableAttributeMap}
     */
    default E get(A attribute) {
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            E next = iterator.next();
            if (next.getAttribute().equals(attribute)) {
                return next;
            }
        }
        throw new NoSuchElementException();
    }
    
    @Override
    Iterator<E> iterator();

}
