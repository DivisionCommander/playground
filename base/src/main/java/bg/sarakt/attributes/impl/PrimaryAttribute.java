/*
 * PrimaryAttributes.java
 *
 * created at 2023-11-23 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.util.EnumSet;
import java.util.Set;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.base.exceptions.UnknownValueException;
import bg.sarakt.characters.Level;

public enum PrimaryAttribute implements Attribute
{

    // Physical
    /**
     * Physical Attributes: {@link Attribute#PHYSIQUESliders that hold information
     * about person}
     */
    STRENGTH(
            "Strength", "STR", AttributeGroup.PHYSICAL, "Basic physical strangth. Affect force attacks and (gear) lifting and carring capacities."
    ),
    AGILITY(
            "Fine Motoric", "FMS", AttributeGroup.PHYSICAL, "Basic fine motoric skills. Affects delicate, fast and multi-movement attacks."
    ),
    CONSTITUTION("STAMINA", "STA", AttributeGroup.PHYSICAL, "Basic ability to take damage without die."),

    // Mental
    /**
     * Mental Attributes: {@link Attribute#PSYCHE}
     */
    INTELLIGENCE("Intelligence", "INT", AttributeGroup.PSYCHICAL, "The ability to learn new thing and solve problems."),
    WISDOM("WISDOM", "WIS", AttributeGroup.PSYCHICAL, "Identify thing, Understand more complicated spells"),
    PSIONIC("Psionic", "PSI", AttributeGroup.PSYCHICAL, "Increace effectiveness of certain magical and mental abilities"),

    /**
     * Personality attributes: {@link Attribute#PERSONALITY}
     */
    SPIRIT("Spirit", "SPI", AttributeGroup.PERSON, "Ability to recover from magical, mental and spiritual affects"),
    WILL(
            "Will Power", "WIL", AttributeGroup.PERSON, "Projecting will over another character of protect from influence from person, artifacts and etc."
    ),

    ;

    private static final Set<PrimaryAttribute> ALL_BASICS = Set.copyOf(EnumSet.allOf(PrimaryAttribute.class));

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

    public PrimaryAttributeEntry getEntry(Number initialValue, Level levelArg) {
        Number value = (initialValue != null) ? initialValue : PrimaryAttributeEntry.getDefaultValue();
        Level level = levelArg != null ? levelArg : Level.TEMP;
        return new PrimaryAttributeEntry(this, value, level);
    }


    public static PrimaryAttribute ofAbbreviation(String abbr) {
        for (PrimaryAttribute a : ALL_BASICS) {
            if (a.abbreviation.equals(abbr)) {
                return a;
            }
        }
        throw new UnknownValueException("The attribute abbreviation " + abbr + " is unknown or unsupported, or is not basic attribute.");
    }

    public static Set<PrimaryAttribute> getAllPrimaryAttributes() { return ALL_BASICS; }

    public static int count() {
        return ALL_BASICS.size();
    }
}
