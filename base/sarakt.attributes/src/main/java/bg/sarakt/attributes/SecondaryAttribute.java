/*
 * SecondaryAttribute.java
 *
 * created at 2023-11-29 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.attributes.impl.PrimaryAttributeEntry;
import bg.sarakt.attributes.impl.SecondaryAttributeEntry;
import bg.sarakt.attributes.impl.SecondaryAttributeImpl;
import bg.sarakt.attributes.levels.Level;

public sealed interface SecondaryAttribute extends Attribute, Comparable<Attribute> permits SecondaryAttributeImpl {

    long getId();

    AttributeFormula getFormula(int level);

    /**
     *
     * @param level
     * @return
     *
     * @since version 0.0.3
     */
    default AttributeFormula getFormula(Level level) {
        return getFormula(level.getLevelNumber());
    }

    SecondaryAttributeEntry getEntry(IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primaryAttributes);

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    default int compareTo(Attribute attribute2) {
        return Attribute.getComparator().compare(this, attribute2);
    }
}