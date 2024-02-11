/*
 * ResourceAttribute.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.resources;

import java.math.BigDecimal;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.primary.PrimaryAttributeEntry;
import bg.sarakt.attributes.resources.impl.ResourceAttributeImpl;

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
