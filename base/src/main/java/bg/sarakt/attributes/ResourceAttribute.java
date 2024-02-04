/*
 * ResourceAttribute.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.math.BigDecimal;

import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.attributes.impl.ResourceAttributeEntry;
import bg.sarakt.attributes.impl.ResourceAttributeImpl;
import bg.sarakt.attributes.levels.Level;

public sealed interface ResourceAttribute extends Attribute, Comparable<Attribute> permits ResourceAttributeImpl {

    long getId();

    PrimaryAttribute getPrimaryAttribute();

    BigDecimal getCoefficientForLevel(Level level);

    ResourceAttributeEntry getEntry(AttributeMapEntry<PrimaryAttribute> primaryAttributeEntry);

    /**
     *
     * @deprecated dropping support of {@link Level} and
     *             {@link bg.sarakt.characters.Level} as now
     *             {@link CharacterAttributeMap} would manage leveling of
     *             {@link Attribute}s and their {@link AttributeMapEntry}
     */
    @Deprecated(forRemoval = true, since = "0.0.7")
    ResourceAttributeEntry getEntry(AttributeMapEntry<PrimaryAttribute> primaryAttributeEntry, Level level);

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    default int compareTo(Attribute o) {
        return Attribute.getComparator().compare(this, o);
    }
}
