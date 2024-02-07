/*
 * LevelNodeImpl.java
 *
 * created at 2024-01-29 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.attributes.levels.LevelNode;

public class LevelNodeImpl implements LevelNode {

    private  BigInteger threshold;
    private final int                                          level;
    private final Map<PrimaryAttribute, BigInteger>            permanentBonues;
    private final Map<Attribute, AttributeModifier<Attribute>> mods;

    LevelNodeImpl(int level) {
        this.level = level;
        this.permanentBonues = new EnumMap<>(PrimaryAttribute.class);
        this.mods = new HashMap<>();
    }

    /*
     * public LevelNodeImpl(LevelNodeEntity entity) { this(entity.getLevel());
     * PrimaryAttributeValuesEntity primary = entity.getPrimary();
     * permanentBonues.putAll(primary.toMap()); List<AdditionalAttrValueEntity>
     * additional = entity.getAdditional();
     * 
     * for (AdditionalAttrValueEntity e : additional) { Attribute a =
     * AttributeFactory.getInstance().ofName(e.getAttribute());
     * AttributeModifier<Attribute> at = new AttrMod<>(a, e.getValue()); mods.put(a,
     * at); } }
     */

    /**
     * @see bg.sarakt.attributes.levels.LevelNode#experienceThreshold()
     */
    @Override
    public BigInteger experienceThreshold() { return threshold;}

    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getPermanentBonuses()
     */
    @Override
    public Map<PrimaryAttribute, BigInteger> getPermanentBonuses() { return permanentBonues; }

    /**
     *
     * @see bg.sarakt.attributes.levels.LevelNode#getLevelNumber()
     */
    @Override
    public Integer getLevelNumber() { return level; }

    private class AttrMod<A extends Attribute> implements AttributeModifier<A> {

        private final A          attr;
        private final BigDecimal value;

        private AttrMod(A attr, BigDecimal value) {
            this.attr = attr;
            this.value = value;
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
        public ModifierType getBonusType() { return ModifierType.FLAT_VALUE; }

        /**
         * @see bg.sarakt.attributes.AttributeModifier#getLayer()
         */
        @Override
        public ModifierLayer getLayer() { return ModifierLayer.CLASS_LAYER; }
    }

    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getAllModifiers()
     */
    @Override
    public List<AttributeModifier<Attribute>> getAllModifiers() { return Collections.emptyList();}
}
