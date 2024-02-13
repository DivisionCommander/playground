/*
 * LevelNodeImpl.java
 *
 * created at 2024-02-13 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.mapping;

import java.math.BigInteger;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.base.utils.ForRemoval;

public class PreloadedLevelNodeImpl implements LevelNode {
    
    private final BigInteger                         experienceThreshold;
    private final int                                points;
    private final int                                number;
    private final Map<PrimaryAttribute, BigInteger>  primaryBonuses;
    private final List<AttributeModifier<Attribute>> mods;
    private Optional<LevelNode>                      previousNode;
    private Optional<LevelNode>                      nextNode;
    
    void addPrimaryBonuses(Map<PrimaryAttribute, BigInteger> bonuses) {
        this.primaryBonuses.putAll(bonuses);
    }
    
    void addModifier(AttributeModifier<Attribute> modifier) {
        this.mods.add(modifier);
    }
    
    void addModifiers(Collection<AttributeModifier<Attribute>> modifiers) {
        this.mods.addAll(modifiers);
    }
    
    void setPrevious(LevelNode node) { this.previousNode = Optional.ofNullable(node); }
    
    void setNext(LevelNode node) { this.nextNode = Optional.ofNullable(node); }
    
    void clearPrevious() {
        if (previousNode.isPresent() && previousNode.get() instanceof PreloadedLevelNodeImpl lni) {
            lni.clearPrevious();
        }
        previousNode = Optional.empty();
    }
    
    PreloadedLevelNodeImpl(Number experience, int points, int number) {
        super();
        this.experienceThreshold = BigInteger.valueOf(experience.longValue());
        this.points = points;
        this.number = number;
        this.primaryBonuses = new EnumMap<>(PrimaryAttribute.class);
        this.mods = new LinkedList<>();
        this.previousNode = Optional.empty();
        this.nextNode = Optional.empty();
    }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#experienceThreshold()
     */
    @Override
    public BigInteger experienceThreshold() {
        return experienceThreshold;
    }
    
    /**
     * 
     * @return
     * 
     * @deprecated Would be merged with {@link LevelNode#getAllModifiers()} with new
     *             as new {@link AttributeModifier} with newly introduced
     *             {@link ModifierType#PRIMARY_PERMANENT}
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5", description = "Would be merged with #getAllModifiers under new ModifierType#permanentPrimary")
    public Map<PrimaryAttribute, BigInteger> getPermanentBonuses() { return primaryBonuses; }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getAllModifiers()
     */
    @Override
    public List<AttributeModifier<Attribute>> getAllModifiers() { return mods; }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getLevelNumber()
     */
    @Override
    public Integer getLevelNumber() { return number; }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getUnallocatedPoints()
     */
    @Override
    public int getUnallocatedPoints() { return points; }
    
    @Override
    public Optional<LevelNode> getPreviousNode() { return this.previousNode; }
    
    @Override
    public Optional<LevelNode> getNextNode() { return this.nextNode; }
}