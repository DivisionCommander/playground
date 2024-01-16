/*
 * Attributes.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.util.Comparator;

import bg.sarakt.attributes.impl.SimpleAttribute;

public interface Attribute {

    Attribute PHYSIQUE    = new SimpleAttribute("Physique", "PH", AttributeType.PHYSICAL, "");
    Attribute PSYCHE      = new SimpleAttribute("Psyche", "PS", AttributeType.PSYCHICAL, "Ability to continue works under stress.");
    Attribute PERSONALITY = new SimpleAttribute("Personality", "PN", AttributeType.PERSON, "Sliders that hold information about person");
    // Needs rework
    // public Attribute UNALLOCATED = new SimpleAttribute("Unallocated points",
    // "UL", "Point still not allocated to any attribute");

    String fullName();

    String abbreviation();

    AttributeType type();

    String description();

    static Comparator<Attribute> getComparator() {
        return new Comparator<Attribute>()
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

    static enum AttributeType
    {
        PHYSICAL,
        PSYCHICAL,
        PERSON;
    }
}
