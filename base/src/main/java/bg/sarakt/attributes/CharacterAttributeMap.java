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
}



