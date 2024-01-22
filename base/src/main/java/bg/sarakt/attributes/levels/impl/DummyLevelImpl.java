/*
 * DummyLevelImpl.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels.impl;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.characters.Level;

@Dummy
public class DummyLevelImpl implements Level {

    private final AtomicInteger levelNumber = new AtomicInteger(1);

    @Override
    public Map<PrimaryAttribute, BigDecimal> getPermanentAttributesBonuses() {
        EnumMap<PrimaryAttribute, BigDecimal> map = new EnumMap<>(PrimaryAttribute.class);
        PrimaryAttribute.getAllPrimaryAttributes().stream().forEach(pa -> map.put(pa, BigDecimal.valueOf(3)));
        return map;
    }

    /**
     * @see bg.sarakt.characters.Level#getModifiers(bg.sarakt.attributes.Attribute)
     */
    @Override
    public <A extends Attribute> AttributeModifier<A> getModifiers(A attribute) {
      return new AttrMod<>(attribute, ModifierType.FLAT_VALUE, BigDecimal.ONE);
    }


    @Override
    public AttributeModifier<ResourceAttribute> getResourceBonus(ResourceAttribute resource) {
        return new AttributeModifierImpl(resource, new BigDecimal("0.55"), ModifierType.FLAT_VALUE);
    }

    /**
     * @see bg.sarakt.characters.Level#levelUp()
     */
    public void levelUp() {}

    /**
     * @see bg.sarakt.characters.Level#viewPreviousLevel()
     */
    @Override
    public Level viewPreviousLevel() {
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
    private class AttrMod<A extends Attribute> implements AttributeModifier<A>{

        private final A att;
        private final ModifierType type;
        private final BigDecimal value;

        AttrMod(A attribute, ModifierType type, BigDecimal value){this.att=attribute;this.type= type; this.value = value;}


        /**
         * @see bg.sarakt.attributes.AttributeModifier#getAttribute()
         */
        @Override
        public A getAttribute() { return att;}

        /**
         * @see bg.sarakt.attributes.AttributeModifier#getValue()
         */
        @Override
        public BigDecimal getValue() {return value; }

        /**
         * @see bg.sarakt.attributes.AttributeModifier#getBonusType()
         */
        @Override
        public ModifierType getBonusType() {return this.type;}

        /**
         * @see bg.sarakt.attributes.AttributeModifier#getLayer()
         */
        @Override
        public ModifierLayer getLayer() {return ModifierLayer.getLowestLayer(); }

    }
}
