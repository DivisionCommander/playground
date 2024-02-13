/*
 * LevelImpl.java
 *
 * created at 2024-02-12 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.mapping;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.levels.LevelUp;
import bg.sarakt.attributes.levels.LevelUpRecord;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.base.utils.ForRemoval;

import org.springframework.lang.Nullable;

public class LevelImpl implements Level {
    
    private BigInteger experience;
    private LevelNode  currentNode;
    
    public LevelImpl(LevelNode current, long experience) {
        this.currentNode = current;
        this.experience = BigInteger.valueOf(experience);
    }
    public LevelImpl(LevelNode current) {
        this(current, 0L);
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#gainExperience(java.math.BigInteger)
     */
    @Override
    public LevelUp gainExperience(BigInteger amount) {
        this.experience = experience.add(amount);
        Optional<LevelNode> optional = currentNode.getNextNode();
        if (optional.isEmpty()) {
            return LevelUp.empty();
        }
        LevelNode nextNode = optional.get();
        if (experience.compareTo(nextNode.experienceThreshold()) < 0) {
            return LevelUp.empty();
        }
        
        LevelUp up = new LevelUpRecord(true, BigInteger.valueOf(nextNode.getUnallocatedPoints()), currentNode.getAllModifiers(),
                nextNode.getAllModifiers());
        if (currentNode instanceof PreloadedLevelNodeImpl lni) {
            lni.clearPrevious();
        }
        currentNode = nextNode;
        
        return up;
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#currentExperience()
     */
    @Override
    public BigInteger currentExperience() {
        return experience;
    }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#experienceThreshold()
     */
    @Override
    public BigInteger experienceThreshold() {
        return currentNode.experienceThreshold();
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
    public Map<PrimaryAttribute, BigInteger> getPermanentBonuses() { return currentNode.getPermanentBonuses(); }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getAllModifiers()
     */
    @Override
    public List<AttributeModifier<Attribute>> getAllModifiers() { return currentNode.getAllModifiers(); }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getLevelNumber()
     */
    @Override
    public Integer getLevelNumber() { return currentNode.getLevelNumber(); }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getUnallocatedPoints()
     */
    @Override
    public int getUnallocatedPoints() { return currentNode.getUnallocatedPoints(); }
    
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
     * @see bg.sarakt.attributes.levels.Level#viewPreviousLevel()
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    @Nullable
    public LevelNode viewPreviousLevel() {
        return currentNode.getPreviousNode().isPresent() ? currentNode.getPreviousNode().get() : null;
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#viewCurrentLevel()
     */
    @Override
    public LevelNode viewCurrentLevel() {
        return currentNode;
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#viewNextLevel()
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    @Nullable
    public LevelNode viewNextLevel() {
        return currentNode.getNextNode().isPresent() ? currentNode.getNextNode().get() : null;
    }
    
}
