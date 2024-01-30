/*
 * LevelUp.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.experience.impl.DummyLevelImpl;
import bg.sarakt.attributes.impl.PrimaryAttribute;

public interface Level {

    Map<PrimaryAttribute, BigDecimal> getPermanentAttributesBonuses();

    /**
     *
     * @since 0.0.6
     */
    default List<AttributeModifier<Attribute>> getAllModifiers(){return Collections.emptyList();}

    /**
     *
     * @param resource
     * @return
     *
     * @since 0.0.3
     * @deprecated merged with {@link Level#getAllModifiers()};
     */
    @Deprecated(forRemoval = true, since = "0.0.6")
    AttributeModifier<ResourceAttribute> getResourceBonus(ResourceAttribute resource);

    /**
     * @deprecated merged with {@link Level#getAllModifiers()};
     */
    @Deprecated(forRemoval = true, since = "0.0.6")
    <A extends Attribute> AttributeModifier<A> getModifiers(A attribute);

    Integer getLevelNumber();

    int getUnallocatedPonts();

    boolean earnExperience(BigInteger amount);

    /**
     * @since 0.0.3
     */
    Level viewPreviousLevel();

    /**
     * For symmetry.
     * @return
     * @since 0.0.3
     */
    default Level viewCurrentLevel() {
        return this;
    }

    Level viewNextLevel();

    public static final Level TEMP = new DummyLevelImpl();

}