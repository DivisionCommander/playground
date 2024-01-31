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
import java.util.Set;

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

    /**
     * @deprecated Merged togetther with {@link LevelNode#getModifier(Attribute)}
     *             into {@link LevelNode#getAllModifiers()}
     *
     */
    @Deprecated(forRemoval = true, since = "0.0.7")
    Set<Attribute> affectedAttributes();

    /**
     *
     * @deprecated the possibility for more than one {@link AttributeModifier} per
     *             attribute make this deprecated in favour of
     *             {@link LevelNode#getAllModifiers()}
     *
     */
    @Deprecated(forRemoval = true, since = "0.0.7")
    <A extends Attribute> AttributeModifier<A> getModifier(A attribute);

    Integer getLevelNumber();
}
