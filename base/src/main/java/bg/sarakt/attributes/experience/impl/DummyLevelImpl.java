/*
 * DummyLevelImpl.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.experience.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.concurrent.atomic.AtomicInteger;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.base.utils.LevelCalculator;
import bg.sarakt.characters.Level;

@Dummy
public class DummyLevelImpl implements Level {

    private final AtomicInteger levelNumber = new AtomicInteger(1);
    private BigInteger          experience  = BigInteger.ZERO;

    private NavigableMap<Long, Integer> levels;

    public DummyLevelImpl() {
        levels = LevelCalculator.getInstance().getLevels();
    }

    @Override
    public Map<PrimaryAttribute, BigDecimal> getPermanentAttributesBonuses() {
        EnumMap<PrimaryAttribute, BigDecimal> map = new EnumMap<>(PrimaryAttribute.class);
        PrimaryAttribute.getAllPrimaryAttributes().stream().forEach(pa -> map.put(pa, BigDecimal.valueOf(3)));
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
     * @see bg.sarakt.characters.Level#getUnallocatedPonts()
     */
    @Override
    public int getUnallocatedPonts() { return 5; }

    /**
     * @see bg.sarakt.characters.Level#getModifiers(bg.sarakt.attributes.Attribute)
     */
    @Override
    public <A extends Attribute> AttributeModifier<A> getModifiers(A attribute) {
        return new AttrMod<>(attribute, ModifierType.FLAT_VALUE, BigDecimal.ONE);
    }

    /**
     * WARINING! Applied after {@link Level#viewPreviousLevel()} cause exception
     * when allocate attributes
     *
     * @see bg.sarakt.characters.Level#getResourceBonus(bg.sarakt.attributes.ResourceAttribute)
     */
    @Override
    public AttributeModifier<ResourceAttribute> getResourceBonus(ResourceAttribute resource) {
        return new AttributeModifierImpl(resource, new BigDecimal("0.55"), ModifierType.FLAT_VALUE);
    }

    /**
     * @see bg.sarakt.characters.Level#viewPreviousLevel()
     */
    @Override
    public Level viewPreviousLevel() {
        if(levelNumber.get()==2)
        {
            return new LevelZero();
        }
        return this;
    }

    @Override
    public Level viewNextLevel() {
        return this;
    }

    @Override
    public Integer getLevelNumber() { return levelNumber.get(); }

    @Dummy
    private record AttributeModifierImpl(ResourceAttribute getAttribute, BigDecimal getValue, ModifierType getBonusType)
            implements AttributeModifier<ResourceAttribute> {

        public BigDecimal getValue() { return getValue(); }

        /**
         * @see bg.sarakt.attributes.AttributeModifier#getLayer()
         */
        @Override
        public ModifierLayer getLayer() { return ModifierLayer.getLowestLayer(); }
    }

    @Dummy
    private class AttrMod<A extends Attribute> implements AttributeModifier<A> {

        private final A            att;
        private final ModifierType type;
        private final BigDecimal   value;

        AttrMod(A attribute, ModifierType type, BigDecimal value) {
            this.att = attribute;
            this.type = type;
            this.value = value;
        }

        /**
         * @see bg.sarakt.attributes.AttributeModifier#getAttribute()
         */
        @Override
        public A getAttribute() { return att; }

        /**
         * @see bg.sarakt.attributes.AttributeModifier#getValue()
         */
        @Override
        public BigDecimal getValue() { return value; }

        /**
         * @see bg.sarakt.attributes.AttributeModifier#getBonusType()
         */
        @Override
        public ModifierType getBonusType() { return this.type; }

        /**
         * @see bg.sarakt.attributes.AttributeModifier#getLayer()
         */
        @Override
        public ModifierLayer getLayer() { return ModifierLayer.getLowestLayer(); }

    }

    public BigInteger getExperience() { return experience; }

    private class LevelZero implements Level {

        /**
         * @see bg.sarakt.characters.Level#getPermanentAttributesBonuses()
         */
        @Override
        public Map<PrimaryAttribute, BigDecimal> getPermanentAttributesBonuses() {
            throw new UnsupportedOperationException();
        }

        /**
         * @see bg.sarakt.characters.Level#getResourceBonus(bg.sarakt.attributes.ResourceAttribute)
         */
        @Override
        public AttributeModifier<ResourceAttribute> getResourceBonus(ResourceAttribute resource) {
            // TODO Auto-generated method stub
            return null;
        }

        /**
         * @see bg.sarakt.characters.Level#getModifiers(bg.sarakt.attributes.Attribute)
         */
        @Override
        public <A extends Attribute> AttributeModifier<A> getModifiers(A attribute) {
            return null;
        }

        /**
         * @see bg.sarakt.characters.Level#getLevelNumber()
         */
        @Override
        public Integer getLevelNumber() { return 0; }

        /**
         * @see bg.sarakt.characters.Level#getUnallocatedPonts()
         */
        @Override
        public int getUnallocatedPonts() {  throw new UnsupportedOperationException();}
        /**
         * @see bg.sarakt.characters.Level#earnExperience(java.math.BigInteger)
         */
        @Override
        public boolean earnExperience(BigInteger amount) {  throw new UnsupportedOperationException();
        }

        /**
         * @see bg.sarakt.characters.Level#viewPreviousLevel()
         */
        @Override
        public Level viewPreviousLevel() {
            throw new UnsupportedOperationException();
        }

        /**
         * @see bg.sarakt.characters.Level#viewNextLevel()
         */
        @Override
        public Level viewNextLevel() {
            return this;
        }

    }
}
