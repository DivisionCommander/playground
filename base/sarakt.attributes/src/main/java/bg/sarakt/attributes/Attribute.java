/*
 * Attributes.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.util.Comparator;

import bg.sarakt.attributes.AttributeGroup.BaseAttribute;
import bg.sarakt.attributes.internal.AbstractAttribute;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.resources.ResourceAttribute;
import bg.sarakt.attributes.secondary.SecondaryAttribute;

public sealed interface Attribute permits PrimaryAttribute, ResourceAttribute, SecondaryAttribute, AbstractAttribute, BaseAttribute {

    String fullName();

    String abbreviation();

    AttributeGroup group();

    String description();

    static Comparator<Attribute> getComparator() {
        return (a1, a2) ->
        {
            if (a1.getClass().isEnum()) {
                if (a2.getClass().isEnum()) {
                    return a1.fullName().compareTo(a2.fullName());

                } else {
                    return -1;
                }
            }
            if (a2.getClass().isEnum()) {
                return 1;
            }
            return a1.fullName().compareTo(a2.fullName());
        };
    }
}
