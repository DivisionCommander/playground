/*
 * DummyLevelImpl.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels.impl;

import java.math.BigInteger;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.concurrent.atomic.AtomicInteger;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.services.LevelService;
import bg.sarakt.base.ApplicationContextProvider;
import bg.sarakt.base.utils.Dummy;

@Dummy
public class DummyLevelImpl implements Level, LevelNode {

    private final AtomicInteger levelNumber = new AtomicInteger(1);
    private BigInteger          experience  = BigInteger.ZERO;

    private NavigableMap<Long, Integer> levels;

    public DummyLevelImpl() {
        levels = ApplicationContextProvider.getApplicationContext().getBean(LevelService.class ).getLevels(); 
    }

    @Override
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
            if( levels.lastEntry().getValue() > levelNumber.get()) {
                entry= levels.lastEntry();
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
     * @see bg.sarakt.attributes.levels.Level#getUnallocatedPonts()
     */
    @Override
    public int getUnallocatedPonts() { return 5; }


    /**
     * @see bg.sarakt.attributes.levels.Level#viewPreviousLevel()
     */
    @Override
    public LevelNode viewPreviousLevel() {
        if(levelNumber.get()==2)
        {
            return new LevelZero();
        }
        return this;
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
         */
        @Override
        public Map<PrimaryAttribute, BigInteger> getPermanentBonuses() {
            throw new UnsupportedOperationException();
        }


        /**
         * @see bg.sarakt.attributes.levels.Level#getLevelNumber()
         */
        @Override
        public final Integer getLevelNumber() { return 0; }


        /**
         * @see bg.sarakt.attributes.levels.LevelNode#getAllModifiers()
         */
        @Override
        public List<AttributeModifier<Attribute>> getAllModifiers() { return Collections.emptyList(); }


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
    public List<AttributeModifier<Attribute>> getAllModifiers() { return Level.super.getAllModifiers(); }

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
}
