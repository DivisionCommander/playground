/*
 * AbstractAttributeMapEntry.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.primary;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.internal.AbstractAttributeMapEntry;

public sealed class PrimaryAttributeEntry extends AbstractAttributeMapEntry<PrimaryAttribute> implements AttributeMapEntry<PrimaryAttribute>
        permits ExperienceEntryImpl {

    private static final AtomicInteger DEFAULT_VALUE = new AtomicInteger(10);

    private BigInteger basicValue;

    PrimaryAttributeEntry(PrimaryAttribute pa, Number initialValue) {
        super(pa);
        int init = initialValue != null ? initialValue.intValue() : DEFAULT_VALUE.get();
        basicValue = BigInteger.valueOf(init);
    }

    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeMapEntry#addModifier(bg.sarakt.attributes.AttributeModifier)
     */
    @Override
    public void addModifier(AttributeModifier<PrimaryAttribute> modifier) {
        if (modifier.getBonusType() == ModifierType.PRIMARY_PERMANENT) {
            addPermanentBonus(modifier.getValue());
            return;
        }
        super.addModifier(modifier);
    }
    
    @Override
    protected void addModifier(AttributeModifier<PrimaryAttribute> modifier, boolean doRecalculate) {
        if (modifier.getBonusType() == ModifierType.PRIMARY_PERMANENT) {
            addPermanentBonus(modifier.getValue());
            if (doRecalculate) {
                recalculate();
            }
            return;
        }
        super.addModifier(modifier, doRecalculate);
    }

    public void addPermanentBonus(BigInteger bonus) {
        this.basicValue = this.basicValue.add(bonus);
        recalculate();
    }
    
    public void addPermanentBonus(BigDecimal bonus) {
        this.basicValue = this.basicValue.add(bonus.toBigInteger());
        recalculate();
    }
    
    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeMapEntry#getBaseValue()
     */
    @Override
    public BigDecimal getBaseValue() { return new BigDecimal(basicValue);}

    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeMapEntry#getBaseValue()
     */
    @Override
    protected BigDecimal getBaseValueForLayer(ModifierLayer layer) {
        Optional<ModifierLayer> lowerLayer = layer.lowerLayer();
        if (lowerLayer.isPresent()) {
            return getValueForLayer(lowerLayer.get());
        }
        return getBaseValue();
    }


    public static Integer getDefaultValue() { return DEFAULT_VALUE.get(); }

    public static void setDefaultValue(int newValue) {
        if (newValue > 0) {
            DEFAULT_VALUE.set(newValue);
        }
    }

    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeMapEntry#hashCode()
     */
    @Override
    public int hashCode() {
        return getAttribute().hashCode();
    }
    
    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeMapEntry#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (other instanceof AttributeMapEntry<?> ame) {
            return getAttribute().equals(ame.getAttribute());
        }
        return false;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Entry for Primary Attribute= ["+attr.fullName() + "] Base value= "+ basicValue;
    }
}
