/*
 * LevelNodeImpl.java
 *
 * created at 2024-02-09 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.services.LevelService;
import bg.sarakt.base.ApplicationContextProvider;
import bg.sarakt.base.Pair;
import bg.sarakt.base.Pair.PairImpl;
import bg.sarakt.base.utils.ForRemoval;

public class LevelNodeImpl implements LevelNode {
    
    private final long                               classId;
    private final BigInteger                         threshold;
    private final Integer                            level;
    private final Integer                            unallocatedPoints;
    private final List<AttributeModifier<Attribute>> allModifiers;
    private boolean                                  retrievedNext     = false;
    private boolean                                  retrievedPrevious = false;
    private Optional<LevelNode>                      nextNode          = Optional.empty();
    private Optional<LevelNode>                      previousNode      = Optional.empty();
    
    public LevelNodeImpl(int levelNumber, long classId, BigInteger thresholdArg, int unallocatedPoints,
            Collection<AttributeModifier<Attribute>> modifiers) {
        this.classId = classId;
        this.threshold = thresholdArg;
        this.level = levelNumber;
        this.unallocatedPoints = unallocatedPoints;
        this.allModifiers = List.copyOf(modifiers);
    }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getPermanentBonuses()
     * 
     * @deprecated Would be merged with {@link LevelNode#getAllModifiers()} with new
     *             as new {@link AttributeModifier} with newly introduced
     *             {@link ModifierType#PRIMARY_PERMANENT}
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5", description = "Would be merged with #getAllModifiers under new ModifierType#permanentPrimary")
    public Map<PrimaryAttribute, BigInteger> getPermanentBonuses() {
        return allModifiers.stream().filter(this::filter).map(this::map).filter(Objects::nonNull).collect(Collectors.toMap(Pair::left, Pair::right));
    }
    
    /**
     * 
     * Must be removed along with {@link LevelNodeImpl#getPermanentBonuses()}
     */
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5", description = "support for the deprecated finctionallity")
    private boolean filter(AttributeModifier<Attribute> mod) {
        if (mod.getBonusType() != ModifierType.PRIMARY_PERMANENT) {
            return false;
        }
        if (mod.getLayer() != ModifierLayer.getLowestLayer()) {
            return false;
        }
        if ( !(mod.getAttribute() instanceof PrimaryAttribute)) {
            return false;
        }
        return mod.getValue() != null && mod.getValue().signum() > 0;
    }
    
    /**
     * 
     * Must be removed along with {@link LevelNodeImpl#getPermanentBonuses()}
     */
    
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5", description = "support for the deprecated finctionallity")
    private Pair<PrimaryAttribute, BigInteger> map(AttributeModifier<Attribute> mod) {
        BigInteger value = mod.getValue().toBigInteger();
        if (mod.getAttribute() instanceof PrimaryAttribute pa) {
            return new PairImpl<>(pa, value);
        }
        // Should not happen due to previously applied
        // filter(AttribuModifier<Attribute>)
        return null;
    }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#experienceThreshold()
     */
    @Override
    public BigInteger experienceThreshold() {
        return this.threshold;
    }
    
    /**
     * 
     * @see bg.sarakt.attributes.levels.LevelNode#getLevelNumber()
     */
    @Override
    public Integer getLevelNumber() { return this.level; }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getNextNode()
     */
    @Override
    public Optional<LevelNode> getNextNode() {
        if (nextNode.isEmpty()) {
            retrieveNext();
        }
        return this.nextNode;
    }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getPreviousNode()
     */
    @Override
    public Optional<LevelNode> getPreviousNode() {
        if (previousNode.isEmpty()) {
            retrievePrevious();
        }
        return previousNode;
    }
    
    private void retrievePrevious() {
        if (retrievedPrevious) {
            return;
        }
        previousNode = retrieveNode(level - 1);
        retrievedNext = true;
        
    }
    
    private void retrieveNext() {
        if (retrievedNext) {
            return;
        }
        nextNode = retrieveNode(level + 1);
        retrievedNext = true;
    }
    
    private Optional<LevelNode> retrieveNode(int level) {
        LevelService service = ApplicationContextProvider.getApplicationContext().getBean(LevelService.class);
        return Optional.ofNullable(service.getLevelNode(classId, level));
    }
    
    /**
     * 
     * @see bg.sarakt.attributes.levels.LevelNode#getUnallocatedPoints()
     */
    @Override
    public int getUnallocatedPoints() { return unallocatedPoints; }
    
    /**
     * 
     * @see bg.sarakt.attributes.levels.LevelNode#getAllModifiers()
     */
    @Override
    public List<AttributeModifier<Attribute>> getAllModifiers() { return allModifiers; }
}
