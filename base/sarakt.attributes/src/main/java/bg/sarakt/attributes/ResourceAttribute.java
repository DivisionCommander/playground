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
import bg.sarakt.attributes.impl.PrimaryAttributeEntry;
import bg.sarakt.attributes.impl.ResourceAttributeEntry;
import bg.sarakt.attributes.impl.ResourceAttributeImpl;
import bg.sarakt.attributes.levels.Level;

public sealed interface ResourceAttribute extends Attribute, Comparable<Attribute> permits ResourceAttributeImpl {
    
    long getId();
    
    PrimaryAttribute getPrimaryAttribute();
    
    BigDecimal getCoefficientForLevel(int level);
    
    BigDecimal getCoefficientForLevel(Level level);
    
    
    /**
     * @since 0.0.12
     */
    ResourceAttributeEntry getEntry(IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> map);
    
    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    default int compareTo(Attribute o) {
        return Attribute.getComparator().compare(this, o);
    }
}
