/*
 * DummyLevelImpl.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.concurrent.atomic.AtomicInteger;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.impl.AttributeModifierRecord;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.levels.LevelUp;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.services.LevelService;
import bg.sarakt.base.ApplicationContextProvider;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.base.utils.ForRemoval;

import org.springframework.lang.Nullable;

/**
 * Placeholder implementation until the real one roll out.
 * 
 * @deprecated Use {@link LevelImpl} instead as it provide full and real
 *             functionality.
 */
@Dummy
@Deprecated(since = "0.1.0-ALPHA")
@ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5", description = "Already outdated in favour of fully implemented LevelImpl")
public class DummyLevelImpl implements Level, LevelNode {
    
    private final AtomicInteger         levelNumber   = new AtomicInteger(1);
    private BigInteger                  experience    = BigInteger.ZERO;
    private LevelNode                   previousLevel = new LevelZero();
    private NavigableMap<Long, Integer> levels;
    
    public DummyLevelImpl() {
        levels = ApplicationContextProvider.getApplicationContext().getBean(LevelService.class).getLevels();
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#gainExperience(java.math.BigInteger)
     */
    @Override
    @Dummy(since = "0.1.0-ALPHA", to = "0.1.5")
    public LevelUp gainExperience(BigInteger amount) {
        return new LevelUp()
        {
            @Override
            public boolean levelUp() {
                return false;
            }
            
            @Override
            public BigInteger unallocatedPoints() {
                return unallocatedPoints();
            }
            
            @Override
            public Collection<AttributeModifier<Attribute>> toAdd() {
                return Collections.emptyList();
            }
            
            @Override
            public Collection<AttributeModifier<Attribute>> toRemove() {
                return Collections.emptyList();
            }
            
        };
    }
    
    /**
     * 
     * @return
     * 
     * @deprecated Would be merged with {@link LevelNode#getAllModifiers()} or
     *             {@link Level#getAllModifiers()} with new as new
     *             {@link AttributeModifier} with newly introduced
     *             {@link ModifierType#PRIMARY_PERMANENT}
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5", description = "Would be merged with #getAllModifiers under new ModifierType#permanentPrimary")
    public Map<PrimaryAttribute, BigInteger> getPermanentBonuses() {
        EnumMap<PrimaryAttribute, BigInteger> map = new EnumMap<>(PrimaryAttribute.class);
        PrimaryAttribute.getAllPrimaryAttributes().stream().forEach(pa -> map.put(pa, BigInteger.valueOf(3)));
        return map;
    }
    
    @Override
    public boolean earnExperience(BigInteger amount) {
        experience = experience.add(amount);
        Entry<Long, Integer> entry = levels.ceilingEntry(experience.longValue());
        if (entry == null) {
            if (levels.lastEntry().getValue() > levelNumber.get()) {
                entry = levels.lastEntry();
            }
            if (entry == null) {
                return false;
            }
        }
        if (entry.getValue() > levelNumber.get()) {
            int newLevel = entry.getValue();
            levelNumber.set(newLevel);
            return true;
        }
        return false;
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#getUnallocatedPoints()
     */
    @Override
    public int getUnallocatedPoints() { return 5; }
    
    /**
     * @deprecated Use {@link LevelNode#getPreviousNode()} instead.
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    @Nullable
    public LevelNode viewPreviousLevel() {
        return previousLevel;
    }
    
    @Override
    public LevelNode viewNextLevel() {
        return this;
    }
    
    @Override
    public Integer getLevelNumber() { return levelNumber.get(); }
    
    public BigInteger getExperience() { return experience; }
    
    private class LevelZero implements LevelNode {
        
        /**
         * @see bg.sarakt.attributes.levels.Level#getPermanentBonuses()
         * 
         * @deprecated Would be merged with {@link LevelNode#getAllModifiers()} under
         *             the new {@link ModifierType#PRIMARY_PERMANENT}.
         */
        @Override
        @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
        @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5", description = "Would be merged with #getAllModifiers under new ModifierType#permanentPrimary")
        public Map<PrimaryAttribute, BigInteger> getPermanentBonuses() {
            throw new UnsupportedOperationException();
        }
        
        /**
         * @see bg.sarakt.attributes.levels.Level#getLevelNumber()
         */
        @Override
        public final Integer getLevelNumber() { return 0; }
        
        /**
         * @see bg.sarakt.attributes.levels.LevelNode#getUnallocatedPoints()
         */
        @Override
        public int getUnallocatedPoints() { return 0; }
        
        /**
         * @see bg.sarakt.attributes.levels.LevelNode#getAllModifiers()
         */
        @Override
        public List<AttributeModifier<Attribute>> getAllModifiers() {
            
            return Collections.emptyList();
        }
        
        /**
         * @see bg.sarakt.attributes.levels.LevelNode#experienceThreshold()
         */
        @Override
        public BigInteger experienceThreshold() {
            return BigInteger.ZERO;
        }
        
    }
    
    @Override
    public BigInteger experienceThreshold() {
        return BigInteger.ZERO;
    }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getAllModifiers()
     */
    @Override
    public List<AttributeModifier<Attribute>> getAllModifiers() {
        return getPermanentBonuses().entrySet().stream().map(this::map).toList();
        
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#viewCurrentLevel()
     */
    @Override
    public LevelNode viewCurrentLevel() {
        return this;
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#currentExperience()
     */
    @Override
    public BigInteger currentExperience() {
        return experience;
    }
    
    private AttributeModifier<Attribute> map(Entry<PrimaryAttribute, BigInteger> entry) {
        return new AttrMod<>(entry.getKey(), new BigDecimal(entry.getValue()), ModifierLayer.BASELINE_LAYER, ModifierType.PRIMARY_PERMANENT);
    }
    
    /**
     * 
     * @deprecated Functionality transfered to {@link AttributeModifierRecord} as
     *             public implementation
     */
    @Dummy(to = "0.1.5")
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(expectedRemovalVersion = "0.1.5", description = "Functionality trancsfered to a record")
    private class AttrMod<A extends Attribute> implements AttributeModifier<A> {
        
        private final A          attr;
        private final BigDecimal value;
        private ModifierLayer    layer = ModifierLayer.CLASS_LAYER;
        private ModifierType     type  = ModifierType.FLAT_VALUE;
        
        private AttrMod(A attr, BigDecimal value, ModifierLayer layer, ModifierType type) {
            super();
            this.attr = attr;
            this.value = value;
            this.layer = layer;
            this.type = type;
        }
        
        /**
         * @see bg.sarakt.attributes.AttributeModifier#getAttribute()
         */
        @Override
        public A getAttribute() { return attr; }
        
        /**
         * @see bg.sarakt.attributes.AttributeModifier#getValue()
         */
        @Override
        public BigDecimal getValue() { return value; }
        
        /**
         * @see bg.sarakt.attributes.AttributeModifier#getBonusType()
         */
        @Override
        public ModifierType getBonusType() { return type; }
        
        /**
         * @see bg.sarakt.attributes.AttributeModifier#getLayer()
         */
        @Override
        public ModifierLayer getLayer() { return this.layer; }
        
    }
    
}