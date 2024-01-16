/*
 * AbstractAttributeMapEntry.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.characters.Level;

public class PrimaryAttributeEntry extends AbstractAttributeMapEntry<PrimaryAttribute> implements AttributeMapEntry<PrimaryAttribute> {

    private static final AtomicInteger DEFAULT_VALUE = new AtomicInteger(10);

    private BigDecimal basicValue;

    public PrimaryAttributeEntry(PrimaryAttribute pa) {
        this(pa, DEFAULT_VALUE.get());
    }

    public PrimaryAttributeEntry(PrimaryAttribute pa, Level level) {
        this(pa, DEFAULT_VALUE.get(), Level.TEMP);
    }

    public PrimaryAttributeEntry(PrimaryAttribute pa, Number initialValue) {
        this(pa, initialValue, Level.TEMP);
    }

    public PrimaryAttributeEntry(PrimaryAttribute pa, Number initialValue, Level level) {
        super(pa, level);
        int init = initialValue != null ? initialValue.intValue() : DEFAULT_VALUE.get();
        this.basicValue = BigDecimal.valueOf(init);
    }

    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#levelUp(bg.sarakt.characters.Level)
     */
    @Override
    public void levelUp(Level level) {
        BigDecimal bonusPoints = level.getPrimaryAttributeBonuses().get(attr);
        basicValue = basicValue.add(bonusPoints);
        recalculate(ModifierLayer.BASELINE_LAYER, basicValue);
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBasicValue()
     */
    @Override
    public BigDecimal getBasicValue() { return basicValue; }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBaseValue()
     */
    @Override
    protected BigDecimal getBaseValueForLayer(ModifierLayer layer) {
        Optional<ModifierLayer> lowerLayer = ModifierLayer.getLowerLayer(layer);
        if (lowerLayer.isPresent()) {
            return getValueForLayer(lowerLayer.get());
        }
        return getBasicValue();
    }

    public static Integer getDefaultValue() { return DEFAULT_VALUE.get(); }

    public static void setDefaultValue(int newValue) {
        if (newValue > 0) {
            DEFAULT_VALUE.set(newValue);
        }
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getCurrentValue()
     */
    @Override
    public BigDecimal getCurrentValue() { return super.getCurrentValue(); }
}
