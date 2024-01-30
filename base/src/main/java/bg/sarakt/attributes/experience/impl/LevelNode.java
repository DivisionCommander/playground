/*
 * LevelNode.java
 *
 * created at 2024-01-27 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.attributes.experience.impl;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.impl.PrimaryAttribute;

public interface LevelNode {

    Map<PrimaryAttribute, BigInteger> getPermanentBonuses();

    Set<Attribute> affectedAttributes();

    <A extends Attribute> AttributeModifier<A> getModifier(A attribute);

    int getLevelNumber();
}



