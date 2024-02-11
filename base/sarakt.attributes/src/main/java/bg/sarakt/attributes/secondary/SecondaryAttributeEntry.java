/*
 * SecondaryAttributeEntry.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.secondary;

import java.math.BigDecimal;

import bg.sarakt.attributes.AttributeMapEntry;

public interface SecondaryAttributeEntry extends AttributeMapEntry<SecondaryAttribute> {

    @Override
    public BigDecimal getBaseValue();

}