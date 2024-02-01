/*
 * PrimaryAttributes.java
 *
 * created at 2023-11-23 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.util.Set;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.base.exceptions.UnknownValueException;

public enum PrimaryAttribute implements Attribute
{

    // Physical
    STRENGTH(Attributes.NAME_STRENGTH, "STR", AttributeGroup.PHYSICAL, Attributes.DESC_STRENGTH),
    AGILITY(Attributes.NAME_AGILITY, "FMS", AttributeGroup.PHYSICAL, Attributes.DESC_AGILITY),
    CONSTITUTION(Attributes.NAME_CONSTITUTION, "STA", AttributeGroup.PHYSICAL, Attributes.DESC_CONSTITUTION),

    // Mental
    INTELLIGENCE(Attributes.NAME_INTELLIGENCE, "INT", AttributeGroup.PSYCHICAL, Attributes.DESC_INTELLIGENCE),
    WISDOM(Attributes.NAME_WISDOM, "WIS", AttributeGroup.PSYCHICAL, Attributes.DESC_WISDOM),
    PSIONIC(Attributes.NAME_PSIONIC, "PSI", AttributeGroup.PSYCHICAL, Attributes.DESC_PSIONIC),

    // Personality attributes:
    SPIRIT(Attributes.NAME_SPIRIT, "SPI", AttributeGroup.PERSON, Attributes.DESC_SPIRIT),
    WILL(Attributes.NAME_WILL, "WIL", AttributeGroup.PERSON, Attributes.DESC_WILL),

    ;

    private static final Set<PrimaryAttribute> ALL_PRIMARY_ATTRIBUTES = Set.of(
            STRENGTH, AGILITY, CONSTITUTION,
            INTELLIGENCE, WISDOM, PSIONIC,
            SPIRIT, WILL);

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

    /**
     * @deprecated due to continuous efforts to handle leveling mechanism to {@link CharacterAttributeMap} and remove {@link Level} from all {@link AttributeMapEntry}
     */
    @Deprecated(forRemoval = true, since="0.0.8")
    public PrimaryAttributeEntry getEntry(Number initialValue, Level levelArg) {
        Number value = (initialValue != null) ? initialValue : PrimaryAttributeEntry.getDefaultValue();
        Level level = levelArg != null ? levelArg : Level.TEMP;
        return new PrimaryAttributeEntry(this, value, level);
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
