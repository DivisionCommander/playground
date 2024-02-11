/*
 * ModifierType.java
 *
 * created at 2024-01-14 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import bg.sarakt.attributes.primary.PrimaryAttribute;

public enum ModifierType
{
    FLAT_VALUE,
    COEFFICIENT,
    
    /**
     * For use only with {@link PrimaryAttribute}
     * 
     * @since 0.1.0-ALPHA
     */
    PRIMARY_PERMANENT,
    
}
