/*
 * AttributeModifier.java
 *
 * created at 2024-01-14 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.math.BigDecimal;

public interface AttributeModifier<A extends Attribute> {

    A getAttribute();

    BigDecimal getValue();

    ModifierType getBonusType();

    ModifierLayer getLayer();
}
