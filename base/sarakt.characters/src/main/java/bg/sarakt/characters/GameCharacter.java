/*
 * GameCharacter.java
 *
 * created at 2023-11-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters;

import java.math.BigDecimal;
import java.math.BigInteger;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.base.utils.ForRemoval;
import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.characters.attributes1.AttributeMap;
import bg.sarakt.characters.attributes1.UnitClass;

public interface GameCharacter extends GameUnit {

    UnitClass characterClass();

    long currentExperience();

    /**
     * @deprecated use {@link GameCharacter#earnExperience(BigInteger)} instead.
     */
    @Deprecated(since = "0.0.3", forRemoval = true)
    @ForRemoval(since = "0.0.3", expectedRemovalVersion = "0.0.5")
    void gainExperience(long experience);
    
    void earnExperience(BigInteger amount);
    
    /**
     * @deprecated Use {@link GameCharacter#getAttributeValue(Attribute)}
     */
    @Deprecated(since = "0.0.3", forRemoval = true)
    @ForRemoval(since = "0.0.3", expectedRemovalVersion = "0.0.5", description = "moving to the new implementation")
    AttributeValuePair getAttribute(Attribute attr);

    BigDecimal getAttributeValue(Attribute attr);
    /**
     * 
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    @ForRemoval(expectedRemovalVersion = "0.0.5", description = "Move to the new implementation")
    AttributeMap getAttributeMap();

    Biography getBiography();

    /**
     * @deprecated
     */
    @Deprecated(since = "0.0.3", forRemoval = true)
    @ForRemoval(since = "0.0.3", expectedRemovalVersion = "0.0.5")
    void levelUp(Level level);
}
