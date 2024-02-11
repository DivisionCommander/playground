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
import java.util.Optional;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.base.utils.ForRemoval;

public interface LevelNode {
    
    /**
     *
     * @since 0.0.7
     */
    BigInteger experienceThreshold();
    
    /**
     * 
     * @return
     * 
     * @deprecated Would be merged with {@link LevelNode#getAllModifiers()} with new
     *             as new {@link AttributeModifier} with newly introduced
     *             {@link ModifierType#PRIMARY_PERMANENT}
     */
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5", description = "Would be merged with #getAllModifiers under new ModifierType#permanentPrimary")
    Map<PrimaryAttribute, BigInteger> getPermanentBonuses();
    
    List<AttributeModifier<Attribute>> getAllModifiers();
    
    /**
     * Numeric representation of current level.
     * 
     * @return
     */
    Integer getLevelNumber();
    
    int getUnallocatedPoints();
    
    /**
     * @ @since 0.1.0-ALPHA <br>
     *   default implementation expected to be dropped in 0.1.5
     */
    default Optional<LevelNode> getPreviousNode() { return Optional.empty(); }
    
    /**
     * 
     * @since 0.1.0-ALPHA <br>
     *        default implementation expected to be dropped in 0.1.5
     */
    default Optional<LevelNode> getNextNode() { return Optional.empty(); }
}
