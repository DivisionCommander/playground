/*
 * AttributeModifierRecord.java
 *
 * created at 2024-02-09 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;

public record AttributeModifierRecord<A extends Attribute>(A getAttribute, BigDecimal getValue, ModifierType getBonusType, ModifierLayer getLayer)
        implements AttributeModifier<A> {
    
}
