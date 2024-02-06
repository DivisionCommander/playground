/*
 * CharacterAttributeMap.java
 *
 * created at 2024-01-31 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.attributes;

import java.math.BigInteger;

import bg.sarakt.attributes.impl.PrimaryAttribute;

public interface CharacterAttributeMap extends AttributeMap<Attribute> {
    
    void addPermanentBonus(PrimaryAttribute pa, BigInteger value);

    void earnExperience(BigInteger amount);
    
    int getLevelNumber();
    
    /**
     * Get amount of unallocated points that can be use to increase permanently
     * {@link PrimaryAttribute}s. Usually, they come from levelling up.
     * 
     * 
     * @return
     * 
     * @since 0.0.13
     */
    int unallocatedPoints();
    
    /**
     * Consume some of unallocated points to increase permanently
     * {@link PrimaryAttribute} values.
     * 
     * Consider something like:
     * {@snippet :
     * 
     * void spendUnallocatedPoints(PrimaryAttribute pa, BigInteger value) {
     *     if (value.intValue() <= unallocatedPoints()) {
     *         addPermanentBonus(pa, value);
     *     }
     * }
     * }
     * 
     * @param pa
     * @param value
     * 
     * @since 0.0.13
     */
    void spendUnallocatedPoints(PrimaryAttribute pa, BigInteger value);
}
