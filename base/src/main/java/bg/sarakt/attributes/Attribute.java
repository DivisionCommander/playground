/*
 * Attributes.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.util.Comparator;

public interface Attribute {

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
