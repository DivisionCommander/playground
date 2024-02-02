/*
 * CharacterClassImpl.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1.impls;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.characters.attributes1.AttributeMap;
import bg.sarakt.characters.attributes1.Attributes;
import bg.sarakt.characters.attributes1.Skill;
import bg.sarakt.characters.attributes1.UnitClass;

public class DefaultUnitClassImpl implements UnitClass
{
    /**
     * @see bg.sarakt.characters.attributes1.UnitClass#unitClassId()
     */
    @Override
    public String unitClassId() {
        return "00000000-0000-0000-0000-000000000001";
    }

    /**
     * @see bg.sarakt.characters.attributes1.UnitClass#className()
     */
    @Override
    public String className()
    {
        return "Default Class";
    }

    /**
     * @see bg.sarakt.characters.attributes1.UnitClass#getBasicAttributes()
     */
    // @Override
    public AttributeMap getBasicAttributes()
    {
        return new AttributeMapImpl();
    }

    /**
     * @see bg.sarakt.characters.attributes1.UnitClass#getSkills()
     */
    @Override
    public Collection<Skill> getSkills()
    {
        return Collections.emptySet();
    }

    public Set<AttributeValuePair> getBasicAttributesForLevel(int level)
    {

        Set<AttributeValuePair> bonuses = new HashSet<>();

        for (PrimaryAttribute ba : PrimaryAttribute.getAllPrimaryAttributes())
        {
            bonuses.add(new AttributeValuePair(ba, 1));
        }
        return bonuses;
    }

    /**
     * @see bg.sarakt.characters.attributes1.UnitClass#getAttributesForLevel(int)
     */
    @Override
    public Set<AttributeValuePair> getAttributesForLevel(int newLevel)
    {
        Set<AttributeValuePair> bonuses = getBasicAttributesForLevel(newLevel);
        // bonuses.add(new AttributeValuePair(Attributes.HIT_POINTS, newLevel));
        // FIXME bonuses.add(new AttributeValuePair(Attribute.UNALLOCATED, newLevel));
        return bonuses;
    }

}
