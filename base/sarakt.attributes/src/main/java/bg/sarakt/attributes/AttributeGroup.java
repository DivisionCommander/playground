/*
 * AttributeGroup.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.attributes;

import bg.sarakt.attributes.utils.Attributes;

import org.springframework.lang.Nullable;

public enum AttributeGroup
{
    
    PHYSICAL(Attributes.NAME_PHYSICAL, Attributes.ABBR_PHYSICAL, Attributes.DESC_PHYSICAL),
    PSYCHICAL(Attributes.NAME_PSYCHICAL, Attributes.ABBR_PSYCHICAL, Attributes.DESC_PSYCHICAL),
    PERSON(Attributes.NAME_PERSON, Attributes.ABBR_PERSON, Attributes.DESC_PERSON);
    
    private final String name;
    private final String abbr;
    private final String desc;

    private AttributeGroup() {
        this(null, null, null);
    }
    
    private AttributeGroup(String name, String abbr, String description) {
        this.name = name;
        this.abbr = abbr;
        this.desc = description;
    }
    
    public Attribute asAttribute() {
        return new BaseAttribute(name, abbr, this, desc);
    }

    /**
     * Attempt to resolve provided string to any {@link AttributeGroup} by its
     * Identifier, its {@link Attribute#fullName()} or
     * {@link Attribute#abbreviation()}
     * 
     * @param arg
     * 
     * @return
     */
    @Nullable
    public static AttributeGroup resolve(String arg) {
        try {
            return AttributeGroup.valueOf(arg);
        } catch (IllegalArgumentException | NullPointerException e) {
            // Ignore and try resolve by any additional element
            switch (arg)
            {
            case Attributes.NAME_PERSON, Attributes.ABBR_PERSON:
                return PERSON;
            case Attributes.NAME_PHYSICAL, Attributes.ABBR_PHYSICAL:
                return PHYSICAL;
            case Attributes.NAME_PSYCHICAL, Attributes.ABBR_PSYCHICAL:
                return PSYCHICAL;
            default:
                return null;
            }
        }
    }
    
    record BaseAttribute(String fullName, String abbreviation, AttributeGroup group, String description) implements Attribute {

        @Override
        public String toString() {
            return fullName;
        }
    }
}