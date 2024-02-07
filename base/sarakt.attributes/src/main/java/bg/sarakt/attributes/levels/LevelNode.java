/*
 * LevelNode.java
 *
 * created at 2024-01-27 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.impl.PrimaryAttribute;

public interface LevelNode {

    /**
     *
     * @since 0.0.7
     */
    BigInteger experienceThreshold();

    Map<PrimaryAttribute, BigInteger> getPermanentBonuses();

    List<AttributeModifier<Attribute>> getAllModifiers();


    Integer getLevelNumber();
}
