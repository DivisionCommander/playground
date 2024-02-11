/*
 * SecondaryAttribute.java
 *
 * created at 2023-11-29 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.secondary;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.primary.PrimaryAttributeEntry;
import bg.sarakt.attributes.secondary.impl.SecondaryAttributeImpl;

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