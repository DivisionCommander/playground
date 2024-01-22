/*
 * AttributeMap.java
 *
 * created at 2024-01-16 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.math.BigDecimal;
import java.util.Map;

public interface AttributeMap<A extends Attribute> {

    BigDecimal getBaseValue(A attribute);

    /**
     * Get value of a specified implementation of the {@link Attribute} for the
     * {@link ModifierLayer}.
     *
     * @param attribute
     * @param layer
     * @return
     */
    BigDecimal getAttributeValueForLayer(A attribute, ModifierLayer layer);

    /**
     * Get value of a specified implementation of the {@link Attribute} for the
     * higher {@link ModifierLayer}.
     *
     * @param attribute
     * @return
     */
    BigDecimal getCurrentAttributeValue(A attribute);

    Map<A, BigDecimal> getAllValues();

    void levelUp();

}
