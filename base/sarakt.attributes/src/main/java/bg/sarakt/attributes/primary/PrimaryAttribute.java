/*
 * PrimaryAttributes.java
 *
 * created at 2023-11-23 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.primary;

import java.util.Set;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.utils.Attributes;
import bg.sarakt.base.exceptions.UnknownValueException;


public enum PrimaryAttribute implements Attribute
{

    // Physical
    STRENGTH(Attributes.NAME_STRENGTH, Attributes.ABBR_STRENGTH, AttributeGroup.PHYSICAL, Attributes.DESC_STRENGTH),
    AGILITY(Attributes.NAME_AGILITY, Attributes.ABBR_AGILITY, AttributeGroup.PHYSICAL, Attributes.DESC_AGILITY),
    CONSTITUTION(Attributes.NAME_CONSTITUTION, Attributes.ABBR_CONSTITUTION, AttributeGroup.PHYSICAL, Attributes.DESC_CONSTITUTION),
    
    // Mental
    INTELLIGENCE(Attributes.NAME_INTELLIGENCE, Attributes.ABBR_INTELLIGENCE, AttributeGroup.PSYCHICAL, Attributes.DESC_INTELLIGENCE),
    WISDOM(Attributes.NAME_WISDOM, Attributes.ABBR_WISDOM, AttributeGroup.PSYCHICAL, Attributes.DESC_WISDOM),
    PSIONIC(Attributes.NAME_PSIONIC, Attributes.ABBR_PSIONIC, AttributeGroup.PSYCHICAL, Attributes.DESC_PSIONIC),
    
    // Personality attributes:
    SPIRIT(Attributes.NAME_SPIRIT, Attributes.ABBR_SPIRIT, AttributeGroup.PERSON, Attributes.DESC_SPIRIT),
    WILL(Attributes.NAME_WILL, Attributes.ABBR_WILL, AttributeGroup.PERSON, Attributes.DESC_WILL),
    EXPERIENCE(Attributes.NAME_XP, Attributes.ABBR_XP, AttributeGroup.PERSON, Attributes.DESC_XP)
    {
        
        /**
         * @see bg.sarakt.attributes.primary.PrimaryAttribute#getEntry(java.lang.Number)
         */
        @Override
        public ExperienceEntryImpl getEntry(Number initialValue) {
            return new ExperienceEntryImpl(initialValue);
        }
    },
    ;

    private static final Set<PrimaryAttribute> ALL_PRIMARY_ATTRIBUTES = Set.of(
            STRENGTH, AGILITY, CONSTITUTION,
            INTELLIGENCE, WISDOM, PSIONIC,
            SPIRIT, WILL, EXPERIENCE);

    private final String         name;
    private final String         abbreviation;
    private final AttributeGroup type;
    private final String         description;

    private PrimaryAttribute(String name, String abbr, AttributeGroup type, String description) {
        this.name = name;
        abbreviation = abbr;
        this.type = type;
        this.description = description;
    }

    /**
     *
     * @see bg.sarakt.attributes.Attribute#fullName()
     */
    @Override
    public String fullName() {
        return name;
    }

    /**
     *
     * @see bg.sarakt.attributes.Attribute#abbreviation()
     */
    @Override
    public String abbreviation() {
        return abbreviation;
    }

    /**
     * @see bg.sarakt.attributes.Attribute#group()
     */
    @Override
    public AttributeGroup group() {
        return type;
    }

    /**
     *
     * @see bg.sarakt.attributes.Attribute#description()
     */
    @Override
    public String description() {
        return description;
    }

    public PrimaryAttributeEntry getEntry(Number initialValue) {
        Number value = (initialValue != null) ? initialValue : PrimaryAttributeEntry.getDefaultValue();
        return new PrimaryAttributeEntry(this, value);
    }

    public static PrimaryAttribute ofName(String name) {
        for(PrimaryAttribute pa : ALL_PRIMARY_ATTRIBUTES) {
            if(pa.name.equals(name)) {
                return pa;
            }
        }
        throw new UnknownValueException("Unknown or unsupported attribute <"+name+">.");
    }

    public static PrimaryAttribute ofAbbreviation(String abbr) {
        for (PrimaryAttribute a : ALL_PRIMARY_ATTRIBUTES) {
            if (a.abbreviation.equals(abbr)) {
                return a;
            }
        }
        throw new UnknownValueException("The attribute abbreviation " + abbr + " is unknown or unsupported, or is not basic attribute.");
    }

    public static Set<PrimaryAttribute> getAllPrimaryAttributes() { return ALL_PRIMARY_ATTRIBUTES; }

    public static int count() {
        return ALL_PRIMARY_ATTRIBUTES.size();
    }
}
