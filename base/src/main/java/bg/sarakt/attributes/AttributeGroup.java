/*
 * AttributeGroup.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.attributes;

public enum AttributeGroup
{
    PHYSICAL{
        /**
         * @see bg.sarakt.attributes.AttributeGroup#asAttribute()
         */
        @Override
        public Attribute asAttribute() {
            return new BaseAttribute("Physique", "PH", AttributeGroup.PHYSICAL, "");
        }
    },
    PSYCHICAL{
        /**
         * @see bg.sarakt.attributes.AttributeGroup#asAttribute()
         */
        @Override
        public Attribute asAttribute() {
         return   new BaseAttribute("Psyche", "PS", this,
                    "Ability to continue works under stress.");
        }
    },
    PERSON{
/**
         * @see bg.sarakt.attributes.AttributeGroup#asAttribute()
         */
        @Override
        public Attribute asAttribute() {
return new BaseAttribute("Personality", "PN", this,
        "Sliders that hold information about person");        }
    };


    public  abstract Attribute
    asAttribute();

    record BaseAttribute(String fullName, String abbreviation, AttributeGroup group, String description) implements Attribute {

        @Override
        public String toString() {
            return fullName;
        }
    }
}