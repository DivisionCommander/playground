/*
 * SecondaryAttributes.java
 *
 * created at 2024-01-16 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.util.Set;

public final class SecondaryAttributes {

    private final Set<SecondaryAttribute>    allAttributes = allAttribute();
    private static final SecondaryAttributes INSTANCE      = new SecondaryAttributes();

    public static Set<SecondaryAttribute> getSecondaryAttributes() { return INSTANCE.allAttributes; }

    private SecondaryAttributes() {}

    private static Set<SecondaryAttribute> allAttribute() {
        Set<SecondaryAttribute> attr = Set.of();

        return attr;
    }
}
