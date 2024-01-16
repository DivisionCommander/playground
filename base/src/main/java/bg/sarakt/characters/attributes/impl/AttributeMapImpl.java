/*
 * AttributeMapImpl.java
 *
 * created at 2024-01-13 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes.impl;

import static bg.sarakt.characters.attributes.AttributeBonus.ApplyLevel.*;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.base.Pair;
import bg.sarakt.base.exceptions.UnknownValueException;
import bg.sarakt.characters.attributes.AttributeBonus;
import bg.sarakt.characters.attributes.AttributeBonus.ApplyLevel;
import bg.sarakt.characters.attributes.AttributeBonus.BonusType;
import bg.sarakt.characters.attributes.AttributeMap;

public abstract class AttributeMapImpl implements AttributeMap {

    private Map<PrimaryAttribute, Integer> primaryAttributes;

    private final Map<ApplyLevel, Map<Attribute, Integer>> calculated;
    private final Map<Attribute, Integer>                  calculatedAttributes;
    private final Map<ApplyLevel, Set<Bonus>>              bonusMap;

    private final NavigableMap<ApplyLevel, Map<Attribute, PairImpl1>> navBonuses;

    protected void recalculate(ApplyLevel level) {
        Map<Attribute, Integer> recalculate;
        switch (level)
        {
        case BASELINE:
            calculated.get(BASELINE).clear();
            recalculate = recalculate(primaryAttributes, bonusMap.get(level));
            calculated.put(BASELINE, recalculate);
        case CLASS_LEVEL:
            calculated.get(CLASS_LEVEL).clear();
            recalculate = recalculate(calculated.get(BASELINE), bonusMap.get(level));
            calculated.put(CLASS_LEVEL, recalculate);
        case GEAR_LEVEL:
            calculated.get(GEAR_LEVEL).clear();
            recalculate = recalculate(calculated.get(CLASS_LEVEL), bonusMap.get(level));
            calculated.put(GEAR_LEVEL, recalculate);
        case SPELL_LEVEL:
            calculated.get(SPELL_LEVEL).clear();
            recalculate = recalculate(calculated.get(GEAR_LEVEL), bonusMap.get(level));
            calculated.put(SPELL_LEVEL, recalculate);
        }
    }

    /**
     * @param primaryAttributes2
     * @param set
     * @return
     */
    protected Map<Attribute, Integer> recalculate0(Map<? extends Attribute, PairImpl1> baseValues, Set<Bonus> set) {
        Map<Attribute, PairImpl1> flatValues = new HashMap<>(baseValues);
        Map<Attribute, PairImpl1> coefficients = new HashMap<>();
        for (Bonus bonus : set) {
            BonusType type = bonus.getType();
            Map<Attribute, PairImpl1> currentMap;
            switch (type)
            {

            case COEFFICIENT:
                currentMap = coefficients;
                break;
            case FLAT:
                currentMap = flatValues;
                break;
            default:
                throw new UnknownValueException("Unknown bonus type. Swich statemant fails!");
            }
            currentMap.putIfAbsent(bonus.getAttribute(), new PairImpl1(type, 0));
            PairImpl1 pair = coefficients.get(bonus.getAttribute());
            pair.add(bonus.value);
        }

        return coefficients.keySet().stream().collect(Collectors.toMap(e -> e, e -> calculateValue(flatValues.get(e), coefficients.get(e))));

    }

    private int calculateValue(PairImpl1 value, PairImpl1 coef) {
        if (value == null) {
            return 1;
        }
        if (coef == null) {
            return value.right().intValue();
        }

        return calculateValue(value.right().intValue(), coef.right().doubleValue());
    }

    private int calculateValue(Integer value, Double coef) {
        if (value == null || value == 0) {
            return 1;
        }
        if (coef == null || coef == Double.NaN || coef == 0) {
            return value;
        }
        return roundValue(value.doubleValue() * coef.doubleValue());
    }

    private Map<Attribute, Integer> recalculate(Map<? extends Attribute, Integer> baseValues, Set<Bonus> bonuses) {
        Map<Attribute, Integer> values = new HashMap<>(baseValues);
        Map<Attribute, Double> coefficients = new HashMap<>();
        for (Bonus bonus : bonuses) {
            switch (bonus.getType())
            {
            case COEFFICIENT:
                Double coef = coefficients.getOrDefault(bonus.getAttribute(), 0.0d);
                coef += bonus.getValue().doubleValue();
                coefficients.put(bonus.getAttribute(), coef);
                break;
            case FLAT:
                Integer flatBonus = values.getOrDefault(bonus.getAttribute(), 1);
                flatBonus += bonus.getValue().intValue();
                values.put(bonus.getAttribute(), flatBonus);
                break;

            }
        }
        Map<Attribute, Integer> calculated = new HashMap<>();
        for (Attribute attr : coefficients.keySet()) {
            Double value = values.getOrDefault(attr, 1).doubleValue();
            value *= coefficients.get(attr);
            Integer intValue = roundValue(value);
            calculated.put(attr, intValue);
        }

        values.keySet().stream().filter(key -> !(calculated.containsKey(key))).forEach(key -> calculated.put(key, values.get(key)));
        return calculated;
    }

    protected void recalculate0(ApplyLevel level) {

        Map<Attribute, Integer> recalculate;
        switch (level)
        {
        // case BASELINE:
        // navBonuses.get(BASELINE).clear();
        // primaryAttributes.entrySet().stream().collect(Collectors.toMap(e->
        // e.getKey(), null))
        // recalculate = recalculate0(primaryAttributes, bonusMap.get(level));
        // navBonuses.put(BASELINE, recalculate);
        case BASELINE:
            // FIXME
            break;
        case CLASS_LEVEL:
            calculated.get(CLASS_LEVEL).clear();
            recalculate = recalculate0(navBonuses.get(BASELINE), bonusMap.get(level));
            calculated.put(CLASS_LEVEL, recalculate);
        case GEAR_LEVEL:
            calculated.get(GEAR_LEVEL).clear();
            recalculate = recalculate0(navBonuses.get(CLASS_LEVEL), bonusMap.get(level));
            calculated.put(GEAR_LEVEL, recalculate);
        case SPELL_LEVEL:
            calculated.get(SPELL_LEVEL).clear();
            recalculate = recalculate0(navBonuses.get(GEAR_LEVEL), bonusMap.get(level));
            calculated.put(SPELL_LEVEL, recalculate);
        }

    }

    /**
     * @param value
     * @return
     */
    protected abstract Integer roundValue(Double value);

    /**
     *
     * @see bg.sarakt.characters.attributes.AttributeMap#addBonus(bg.sarakt.characters.attributes.AttributeBonus)
     */
    @Override
    public void addBonus(AttributeBonus bonus) {
        ApplyLevel level = bonus.getApplyLevel();
        Bonus mapBonus = mapBonus(bonus);
        bonusMap.putIfAbsent(level, new HashSet<>());
        bonusMap.get(level).add(mapBonus);
        recalculate(level);
    }

    @Override
    public void removeBonus(AttributeBonus bonus) {
        ApplyLevel level = bonus.getApplyLevel();
        Bonus mapBonus = mapBonus(bonus);
        bonusMap.get(level).remove(mapBonus);
        recalculate(level);;
    }

    /**
     * @param attributeBonus
     * @return
     */
    protected Bonus mapBonus(AttributeBonus bonus) {
        return new Bonus(bonus.getAttribute(), bonus.getValue(), bonus.getBonusType());
    }

    /**
     *
     * @see bg.sarakt.characters.attributes.AttributeMap#getAttributeValue(bg.sarakt.attributes.Attribute)
     */
    @Override
    public Integer getAttributeValue(Attribute attribute) {
        return calculatedAttributes.get(attribute);
    }

    /**
     * @see bg.sarakt.characters.attributes.AttributeMap#getBaseAttributes()
     */
    @Override
    public Map<PrimaryAttribute, Integer> getBaseAttributes() { return Collections.unmodifiableMap(primaryAttributes); }

    /**
     *
     * @see bg.sarakt.characters.attributes.AttributeMap#getAllAttributes()
     */
    @Override
    public Map<Attribute, Integer> getAllAttributes() { return Collections.unmodifiableMap(calculatedAttributes); }

    AttributeMapImpl() {
        primaryAttributes = new EnumMap<>(PrimaryAttribute.class);
        calculatedAttributes = new HashMap<>();
        this.bonusMap = new HashMap<>();
        this.calculated = new HashMap<>();
        this.navBonuses = new TreeMap<>();
    }

    private class PairImpl1 implements Pair<BonusType, Number> {

        private final BonusType type;
        private Number          value;

        protected PairImpl1(BonusType type, Number number) {
            this.type = type;
            this.value = number;
        }

        /**
         * @param value2
         */
        public void add(Number newValue) {
            if (newValue instanceof Integer intValue) {
                Integer value = this.value.intValue() + intValue.intValue();
                this.value = value;
                return;
            }
            if (newValue instanceof Double doubleValue) {
                Double value = this.value.doubleValue() + doubleValue.doubleValue();
                this.value = value;
                return;
            }
            throw new UnsupportedOperationException("Wrong numeric format!");

        }

        /**
         * @see bg.sarakt.base.Pair#left()
         */
        @Override
        public BonusType left() {
            return type;
        }

        /**
         * @see bg.sarakt.base.Pair#right()
         */
        @Override
        public Number right() {
            return value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(type);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof PairImpl1 pair) {
                return this.type == pair.type;
            }
            if (obj instanceof Pair<?, ?> pair) {
                if (pair.left().getClass() != BonusType.class) {
                    return false;
                }
                if ( !(pair.right() instanceof Number)) {
                    return false;
                }

            }

            return false;
        }
    }

    private class Bonus {

        private final Attribute attribute;
        private final BonusType type;
        private final Number    value;

        private Bonus(Attribute attr, Number number, BonusType type) {
            this.attribute = Objects.requireNonNull(attr, "Attribute cannot be null!");
            this.value = number;
            this.type = type;
        }

        private Attribute getAttribute() { return this.attribute; }

        private Number getValue() { return this.value; }

        private BonusType getType() { return this.type; }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Objects.hash(attribute, type, value);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if ( !(obj instanceof Bonus)) {
                return false;
            }
            Bonus other = (Bonus) obj;
            return Objects.equals(attribute, other.attribute) && type == other.type && Objects.equals(value, other.value);
        }
    }

}