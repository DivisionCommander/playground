/*
 * Attributes.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.util.Comparator;

import bg.sarakt.attributes.impl.AttributeFactory;

public interface Attribute {

    //Root Attributes
//    Attribute PHYSIQUE    = AttributeFactory.PHYSIQUE;
//    Attribute PSYCHE      = AttributeFactory.PSYCHE;
//    Attribute PERSONALITY = AttributeFactory.PERSONALITY;


    String fullName();

    String abbreviation();

    AttributeGroup group();

    String description();

    static Comparator<Attribute> getComparator() {
        return new Comparator<>()
        {

            @Override
            public int compare(Attribute a1, Attribute a2) {
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
        };
    }
}
