/*
 * LevelUp.java
 *
 * created at 2024-02-09 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.attributes.levels;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;

public interface LevelUp {
    
    /**
     * Provide information if
     * 
     * @return
     */
    boolean levelUp();
    
    BigInteger unallocatedPoints();
    
    Collection<AttributeModifier<Attribute>> toAdd();
    
    Collection<AttributeModifier<Attribute>> toRemove();
    
    
    public static LevelUp empty() {
        return new LevelUpRecord(false, BigInteger.ZERO, Collections.emptySet(), Collections.emptySet());
    }
}



