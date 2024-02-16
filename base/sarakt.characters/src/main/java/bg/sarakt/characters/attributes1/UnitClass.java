/*
 * CharacterClass.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import bg.sarakt.base.utils.ForRemoval;
import bg.sarakt.characters.attributes.AttributeValuePair;

public interface UnitClass extends Serializable
{
    String unitClassId();

    String className();

    default Collection<Skill> getSkills()
    {
        return Collections.emptySet();
    }

    /**
     * 
     * @param newLevel
     * 
     * @return
     * 
     * @deprecated
     */
    @Deprecated(since = "0.0.3", forRemoval = true)
    @ForRemoval(since = "0.0.3", expectedRemovalVersion = "0.0.5")
    Set<AttributeValuePair> getAttributesForLevel(int newLevel);


}
