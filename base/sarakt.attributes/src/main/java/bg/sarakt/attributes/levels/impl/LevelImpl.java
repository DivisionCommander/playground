/*
 * LevelImpl.java
 *
 * created at 2024-02-09 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels.impl;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.levels.LevelUp;
import bg.sarakt.attributes.levels.LevelUpRecord;
import bg.sarakt.attributes.levels.simple.SimpleLevel;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.base.utils.ForRemoval;

import org.springframework.lang.Nullable;

/**
 * Use {@link SimpleLevel} instead.
 * 
 * @author IceDragon
 */
@Deprecated(since = "0.1.0-ALPHA-4", forRemoval = true)
@ForRemoval(since = "0.1.0-ALPHA-4", expectedRemovalVersion = "0.1.1")
public class LevelImpl implements Level {
    
    private BigInteger experience;
    private LevelNode  currentNode;      // FIXME: currently it doesn't initialize
    private int        unallocatedPoints;
    private BigInteger threshold;
    
    /**
     * @see bg.sarakt.attributes.levels.Level#gainExperience(java.math.BigInteger)
     */
    @Override
    public LevelUp gainExperience(BigInteger amount) {
        experience = experience.add(amount);
        if (experience.compareTo(threshold) < 0 || currentNode.getNextNode().isEmpty()) {
            return LevelUp.empty();
        }
        return doLevelUp();
    }
    
    private LevelUp doLevelUp() {
        
        BigInteger points = BigInteger.ZERO;
        Set<AttributeModifier<Attribute>> toAdd = new HashSet<>();
        Set<AttributeModifier<Attribute>> toRemove = new HashSet<>();
        
        LevelNode old = currentNode;
        Optional<LevelNode> nextNode = old.getNextNode();
        
        while (nextNode.isPresent() && experience.compareTo(nextNode.get().experienceThreshold()) >= 0) {
            toRemove.addAll(old.getAllModifiers());
            LevelNode current = nextNode.get();
            points = points.add(BigInteger.valueOf(current.getUnallocatedPoints()));
            toAdd.addAll(current.getAllModifiers());
            old = current;
        }
        toRemove.removeAll(toAdd);
        toAdd.removeAll(toRemove);
        
        return new LevelUpRecord(true, points, toAdd, toRemove);
    }
    
    LevelImpl() {
        experience = BigInteger.ZERO;
    }
    
    LevelImpl(Number experience) {
        this.experience = BigInteger.valueOf(experience.longValue());
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#currentExperience()
     */
    @Override
    public BigInteger currentExperience() {
        return experience;
    }
    
    /**
     * 
     * @return
     */
    @Override
    public int getUnallocatedPoints() { return this.unallocatedPoints; }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#getAllModifiers()
     */
    @Override
    public List<AttributeModifier<Attribute>> getAllModifiers() { return currentNode.getAllModifiers(); }

    /**
     * @see bg.sarakt.attributes.levels.LevelNode#experienceThreshold()
     */
    @Override
    public BigInteger experienceThreshold(){ return currentNode.experienceThreshold();}
    /**
     * @see bg.sarakt.attributes.levels.Level#getLevelNumber()
     */
    @Override
    public Integer getLevelNumber() { return currentNode.getLevelNumber(); }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#viewCurrentLevel()
     */
    @Override
    public LevelNode viewCurrentLevel() {
        return this;
    }
    
    /**
     * 
     * @see bg.sarakt.attributes.levels.LevelNode#getPermanentBonuses()
     * 
     * @deprecated For removal
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5", description = "Would be merged with #getAllModifiers under new ModifierType#permanentPrimary")
    public Map<PrimaryAttribute, BigInteger> getPermanentBonuses() {
        throw new UnsupportedOperationException("Deprecated");
    }
    
    /**
     * 
     * @deprecated use {@link Level#gainExperience(BigInteger)} instead
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    public boolean earnExperience(BigInteger amount) {
        return gainExperience(amount).levelUp();
    }

    /**
     * @deprecated Use {@link LevelNode#getPreviousNode()} instead.
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    @Nullable
    public LevelNode viewPreviousLevel() {
        Optional<LevelNode> prev = currentNode.getPreviousNode();
        if (prev.isPresent()) {
            return prev.get();
        }
        return null;
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#viewNextLevel()
     * 
     * @deprecated Use {@link LevelNode#getNextNode()} instead.
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    @Nullable
    public LevelNode viewNextLevel() {
        Optional<LevelNode> next = currentNode.getNextNode();
        if (next.isPresent()) {
            return next.get();
        }
        return null;
    }
}