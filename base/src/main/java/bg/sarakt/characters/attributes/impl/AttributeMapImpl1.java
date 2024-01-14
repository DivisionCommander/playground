/*
 * AttributeMapImpl.java
 *
 * created at 2024-01-12 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes.impl;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.base.Pair;
import bg.sarakt.characters.attributes.AttributeBonus;
import bg.sarakt.characters.attributes.AttributeBonus.ApplyLevel;
import bg.sarakt.characters.attributes.AttributeBonus.BonusType;
import bg.sarakt.characters.attributes.AttributeMap;
import bg.sarakt.characters.attributes1.impls.PrimaryAttributes;

public abstract class AttributeMapImpl1 implements AttributeMap {

    /**
     * Baseline attributes from species, race and gender.
     */
    private Map<PrimaryAttributes, Integer> primaryAttributes;
    private Set<AttributeBonus> attributeBonuses;

    AttributeMapImpl1(boolean dummy) {
        this();
    }

    AttributeMapImpl1(boolean dummy, Map<PrimaryAttributes, Integer> map) {
        this(map);
        primaryAttributes = new EnumMap<>(PrimaryAttributes.class);
        attributeBonuses = new HashSet<>();
    }

    /**
     * @see bg.sarakt.characters.attributes.AttributeMap#getBaseAttributes()
     */
    @Override
    public Map<PrimaryAttributes, Integer> getBaseAttributes() { return Collections.unmodifiableMap(primaryAttributes); }

    protected abstract void recalc();

    /**
     *
     * @see bg.sarakt.characters.attributes.AttributeMap#addBonus(bg.sarakt.characters.attributes.AttributeBonus)
     */
    @Override
    public void addBonus(AttributeBonus bonus) {
        this.attributeBonuses.add(bonus);
    }

    /**
     * baseline attributes <br>
     * class line attributes<br>
     * gear line attributes<br>
     * buff line attributes<br>
     *
     */

    private final Map<PrimaryAttributes, Integer>                baselineAttributes1;
    private Map<ApplyLevel, Map<BonusType, Set<AttributeBonus>>> bonuses;

    private Map<Attribute, Integer> attributes;

    AttributeMapImpl1() {
        baselineAttributes1 = new EnumMap<>(PrimaryAttributes.class);
    }

    AttributeMapImpl1(Map<PrimaryAttributes, Integer> primary) {
        this();
        if (primary != null && !primary.isEmpty()) {
            baselineAttributes1.putAll(primary);
        }
    }

    protected void recalculate() {
        attributes.clear();
        Map<BonusType, Set<AttributeBonus>> map = bonuses.get(ApplyLevel.BASELINE);
        Map<Attribute, Number> appliedBonuses = new LinkedHashMap<>(baselineAttributes1);
        Set<AttributeBonus> set;

        set = map.get(BonusType.FLAT);
        recalculate(appliedBonuses, set);
        set = map.get(BonusType.COEFFICIENT);
        recalculate(appliedBonuses, set);

        map = bonuses.get(ApplyLevel.CLASS_LEVEL);
        set = map.get(BonusType.FLAT);
        recalculate(appliedBonuses, set);
        set = map.get(BonusType.COEFFICIENT);
        recalculate(appliedBonuses, set);

        map = bonuses.get(ApplyLevel.GEAR_LEVEL);
        set = map.get(BonusType.FLAT);
        recalculate(appliedBonuses, set);
        set = map.get(BonusType.COEFFICIENT);
        recalculate(appliedBonuses, set);

        map = bonuses.get(ApplyLevel.SPELL_LEVEL);
        set = map.get(BonusType.FLAT);
        recalculate(appliedBonuses, set);
        set = map.get(BonusType.COEFFICIENT);
        recalculate(appliedBonuses, set);
    }

    private void recalculate(Map<Attribute, Number> bonuses, Set<AttributeBonus> set) {
        for (AttributeBonus bonus : set) {
            int value = bonuses.getOrDefault(bonus.getAttribute(), 0).intValue();
            value += bonus.getValue().intValue();
            bonuses.put(bonus.getAttribute(), value);
        }

        for (AttributeBonus bonus : set) {
            double value = bonuses.getOrDefault(bonus.getAttribute(), 0.0d).doubleValue();
            value *= bonus.getValue().doubleValue();
            bonuses.put(bonus.getAttribute(), value);
        }
    }

    private class PairImpl implements Pair<Attribute, Number> {

        private final Attribute attribute;
        private Number          value;

        private PairImpl(Attribute attr, Number value) {
            this.attribute = attr;
            this.value = value;
        }

        /**
         * @see bg.sarakt.base.Pair#left()
         */
        @Override
        public Attribute left() {
            return attribute;
        }

        /**
         * @see bg.sarakt.base.Pair#right()
         */
        @Override
        public Number right() {
            return value;
        }

        private void add(int number) {
            int old = value.intValue();
            old += number;
            value = old;
        }

        private void add(double number) {
            double old = value.doubleValue();
            old += number;
            value = old;
        }

    }

}
