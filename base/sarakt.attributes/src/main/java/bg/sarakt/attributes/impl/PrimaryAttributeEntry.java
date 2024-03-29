/*
 * AbstractAttributeMapEntry.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.levels.Level;

public sealed class PrimaryAttributeEntry extends AbstractAttributeMapEntry<PrimaryAttribute> implements AttributeMapEntry<PrimaryAttribute>
        permits ExperienceEntry {

    private static final AtomicInteger DEFAULT_VALUE = new AtomicInteger(10);

    private BigInteger basicValue;

    PrimaryAttributeEntry(PrimaryAttribute pa, Number initialValue) {
        super(pa);
        int init = initialValue != null ? initialValue.intValue() : DEFAULT_VALUE.get();
        basicValue = BigInteger.valueOf(init);
    }


    public void addPermanentBonus(BigInteger bonus) {
        this.basicValue = this.basicValue.add(bonus);
        recalculate();
        System.out.println(this);
    }
    
    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBaseValue()
     */
    @Override
    public BigDecimal getBaseValue() { return new BigDecimal(basicValue);}

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBaseValue()
     */
    @Override
    protected BigDecimal getBaseValueForLayer(ModifierLayer layer) {
        Optional<ModifierLayer> lowerLayer = layer.lowerLayer();
        if (lowerLayer.isPresent()) {
            return getValueForLayer(lowerLayer.get());
        }
        return getBaseValue();
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getCurrentValue()
     */
    @Override
    public BigDecimal getCurrentValue() { return super.getCurrentValue(); }

    public static Integer getDefaultValue() { return DEFAULT_VALUE.get(); }

    public static void setDefaultValue(int newValue) {
        if (newValue > 0) {
            DEFAULT_VALUE.set(newValue);
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Entry for Primary Attribute= ["+attr.fullName() + "] Base value= "+ basicValue;
    }
}
