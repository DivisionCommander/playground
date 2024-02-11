/*
 * LevelUpRecord.java
 *
 * created at 2024-02-09 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels;

import java.math.BigInteger;
import java.util.Collection;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;

public record LevelUpRecord(boolean levelUp, BigInteger unallocatedPoints, Collection<AttributeModifier<Attribute>> toAdd, Collection<AttributeModifier<Attribute>> toRemove)
        implements LevelUp {
}
