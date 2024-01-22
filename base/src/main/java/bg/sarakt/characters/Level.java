/*
 * LevelUp.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters;

import java.math.BigDecimal;
import java.util.Map;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.attributes.levels.impl.DummyLevelImpl;

public interface Level {

    Map<PrimaryAttribute, BigDecimal> getPermanentAttributesBonuses();

    /**
     *
     * @param resource
     * @return
     *
     * @since 0.0.3
     */
    AttributeModifier<ResourceAttribute> getResourceBonus(ResourceAttribute resource);

    <A extends Attribute> AttributeModifier<A> getModifiers(A attribute);

    Integer getLevelNumber();

    /**
     * @since 0.0.3
     */
    Level viewPreviousLevel();

    /**
     * For symmetry.
     * @return
     * @since 0.0.3
     */
    default Level CurrentLevel() {
        return this;
    }

    Level viewNextLevel();

    public static final Level TEMP = new DummyLevelImpl();

}