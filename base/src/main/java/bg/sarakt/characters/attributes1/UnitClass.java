/*
 * CharacterClass.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.characters.attributes1.impls.DefaultUnitClassImpl;

public interface UnitClass
{
    String unitClassId();

    String className();

    // AttributeMap getBasicAttributes( );

    default Collection<Skill> getSkills()
    {
        return Collections.emptySet();
    }

    Set<AttributeValuePair> getAttributesForLevel(int newLevel);

    static UnitClass getDefault() {return new DefaultUnitClassImpl();}

}
