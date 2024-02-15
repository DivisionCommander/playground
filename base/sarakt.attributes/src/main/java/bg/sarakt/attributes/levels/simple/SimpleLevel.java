/*
 * SimpleLevel.java
 *
 * created at 2024-02-09 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels.simple;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.levels.LevelUp;
import bg.sarakt.attributes.primary.PrimaryAttribute;

public class SimpleLevel implements Level {
    
    /** field <code>DEPRECATED</code> */
    private static final String DEPRECATED = "DEPRECATED!";
    
    private final AtomicInteger         level             = new AtomicInteger(1);
    private final AtomicLong            experience;
    private SimpleLevelNode             currentNode;
    private NavigableMap<Long, Integer> levelThresholds;
    private int                         unallocatedPoints = 0;
    
    public SimpleLevel(Map<Integer, Long> levelMap) {
        this(0L, levelMap);
    }
    
    public SimpleLevel(Number experience, Map<Integer, Long> levelMap) {
        this.experience = new AtomicLong();
        levelThresholds = new TreeMap<>();
        levelMap.entrySet().stream().forEach(e -> levelThresholds.put(e.getValue(), e.getKey()));
        Entry<Long, Integer> lvl = levelThresholds.floorEntry(experience.longValue());
        currentNode = new SimpleLevelNode(lvl.getValue(), BigInteger.valueOf(lvl.getKey()));
        gainExperience(BigInteger.valueOf(experience.longValue()));
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#gainExperience(java.math.BigInteger)
     */
    @Override
    public LevelUp gainExperience(BigInteger amount) {
        long exp = this.experience.addAndGet(amount.longValue());
        
        Entry<Long, Integer> entry = levelThresholds.ceilingEntry(exp);
        if (entry == null || entry.getValue().equals(level.get())) {
            return LevelUp.empty();
        }
        final int points = entry.getValue() - level.get();
        this.unallocatedPoints = points;
        
        LevelNode current = currentNode;
        currentNode.generateNext(BigInteger.valueOf(entry.getKey()));
        
        SimpleLevelNode next = currentNode.getNext();
        Collection<AttributeModifier<Attribute>> toAdd = next == null ? Collections.emptySet() : next.getAllModifiers();
        List<AttributeModifier<Attribute>> toRemove = current.getAllModifiers().stream()
                .filter(m -> m.getBonusType() != ModifierType.PRIMARY_PERMANENT).toList();
        
        LevelUp lvl = new LevelUp()
        {
            
            @Override
            public BigInteger unallocatedPoints() {
                return BigInteger.valueOf(points);
            }
            
            @Override
            public Collection<AttributeModifier<Attribute>> toRemove() {
                return toRemove;
            }
            
            @Override
            public Collection<AttributeModifier<Attribute>> toAdd() {
                return toAdd;
            }
            
            @Override
            public boolean levelUp() {
                return true;
            }
        };
        
        level.set(entry.getValue());
        currentNode = next;
        return lvl;
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#getLevelNumber()
     */
    @Override
    public Integer getLevelNumber() { return level.get(); }
    
    @Override
    public BigInteger currentExperience() {
        return BigInteger.valueOf(this.experience.get());
    }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#experienceThreshold()
     */
    @Override
    public BigInteger experienceThreshold() {
        return BigInteger.valueOf(levelThresholds.ceilingKey(experience.get()));
    }
    
    @Override
    public LevelNode viewCurrentLevel() {
        return this.currentNode;
    }
    
    @Override
    public LevelNode viewPreviousLevel() { // NOSONAR
        throw new UnsupportedOperationException(DEPRECATED);
    }
    
    @Override
    public LevelNode viewNextLevel() {// NOSONAR
        throw new UnsupportedOperationException(DEPRECATED);
    }
    
    /**
     * 
     * @see bg.sarakt.attributes.levels.Level#earnExperience(java.math.BigInteger)
     */
    @Override
    public boolean earnExperience(BigInteger amount) {// NOSONAR
        throw new UnsupportedOperationException(DEPRECATED);
    }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getPermanentBonuses()
     */
    @Override
    public Map<PrimaryAttribute, BigInteger> getPermanentBonuses() {// NOSONAR
        throw new UnsupportedOperationException(DEPRECATED);
    }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getAllModifiers()
     */
    @Override
    public List<AttributeModifier<Attribute>> getAllModifiers() { return currentNode.getAllModifiers(); }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getUnallocatedPoints()
     */
    @Override
    public int getUnallocatedPoints() { return unallocatedPoints; }
    
    /**
     * @see java.lang.Object#toString()
     */
    // @formatter:off
    @Override
    public String toString() {
        return "SimpleLevel [level="
               + this.level
               + ", experience="
               + this.experience
               + "]";
    //@formatter:on
    }
}
