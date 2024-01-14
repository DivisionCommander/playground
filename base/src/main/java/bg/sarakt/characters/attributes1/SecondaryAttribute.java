/*
 * SecondaryAttribute.java
 *
 * created at 2023-11-29 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1;

import bg.sarakt.attributes.Attribute;

public interface SecondaryAttribute extends Attribute, Comparable<Attribute> {

    long getId();

    AttributeFormula getFormula(int level);

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    default int compareTo(Attribute o) {
        return Attribute.getComparator().compare(this, o);
    }
}