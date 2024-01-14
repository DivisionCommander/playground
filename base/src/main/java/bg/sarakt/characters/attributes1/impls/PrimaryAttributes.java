/*
 * PrimaryAttributes.java
 *
 * created at 2023-11-23 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1.impls;

import java.util.EnumSet;
import java.util.Set;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.base.exceptions.UnknownValueException;

public enum PrimaryAttributes implements Attribute
{

    // Physical
    /**
     * Physical Attributes: {@link Attribute#PHYSIQUESliders that hold information
     * about person}
     */
    STRENGTH("Strength", "STR", AttributeType.PHYSICAL, "Basic physical strangth. Affect force attacks and (gear) lifting and carring capacities."),
    AGILITY("Fine Motoric", "FMS", AttributeType.PHYSICAL, "Basic fine motoric skills. Affects delicate, fast and multi-movement attacks."),
    CONSTITUTION("STAMINA", "STA", AttributeType.PHYSICAL, "Basic ability to take damage without die."),

    // Mental
    /**
     * Mental Attributes: {@link Attribute#PSYCHE}
     */
    INTELLIGENCE("Intelligence", "INT", AttributeType.PSYCHICAL, "The ability to learn new thing and solve problems."),
    WISDOM("WISDOM", "WIS", AttributeType.PSYCHICAL, "Identify thing, Understand more complicated spells"),
    PSIONIC("Psionic", "PSI", AttributeType.PSYCHICAL, "Increace effectiveness of certain magical and mental abilities"),

    /**
     * Personality attributes: {@link Attribute#PERSONALITY}
     */
    SPIRIT("Spirit", "SPI", AttributeType.PERSON, "Ability to recover from magical, mental and spiritual affects"),
    WILL("Will Power", "WIL", AttributeType.PERSON,
            "Projecting will over another character of protect from influence from person, artifacts and etc."),

    ;

    private static final Set<PrimaryAttributes> ALL_BASICS = EnumSet.allOf(PrimaryAttributes.class);

    private final String        name;
    private final String        abbreviation;
    private final AttributeType type;
    private String              description;

    private PrimaryAttributes(String name, String abbr, AttributeType type, String description)
    {
        this.name = name;
        this.abbreviation = abbr;
        this.type = type;
    }

    @Override
    public String fullName()
    {
        return this.name;
    }

    @Override
    public String abbreviation()
    {
        return this.abbreviation;
    }

    /**
     * @see bg.sarakt.attributes.Attribute#type()
     */
    @Override
    public AttributeType type()
    {
        return type;
    }

    @Override
    public String description()
    {
        return this.description;
    }

    public static PrimaryAttributes getByAbbreviation(String abbr)
    {
        for (PrimaryAttributes a : ALL_BASICS)
        {
            if (a.abbreviation.equals(abbr))
            {
                return a;
            }
        }
        throw new UnknownValueException("The attribute abbreviation " + abbr + " is unknown or unsupported, or is not basic attribute.");
    }

    public static Set<PrimaryAttributes> getAllPrimaryAttributes()
    {
        return ALL_BASICS;
    }

    public static int count()
    {
        return ALL_BASICS.size();
    }
}
