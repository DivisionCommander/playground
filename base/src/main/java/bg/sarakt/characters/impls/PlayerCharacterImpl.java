/*
 * PlayerCharacterImpl.java
 *
 * created at 2023-11-23 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.impls;

import java.util.Set;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.base.AbstractGameObject;
import bg.sarakt.base.utils.LevelCalculator;
import bg.sarakt.characters.Biography;
import bg.sarakt.characters.GameCharacter;
import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.characters.attributes1.AttributeMap;
import bg.sarakt.characters.attributes1.Attributes;
import bg.sarakt.characters.attributes1.UnitClass;
import bg.sarakt.characters.attributes1.impls.AttributeMapImpl;
import bg.sarakt.characters.attributes1.impls.DynamicUnitClass;
import bg.sarakt.combats.Combatant;
import bg.sarakt.logging.Logger;

public class PlayerCharacterImpl extends AbstractGameObject implements GameCharacter {

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202311230107L;

    private static final String representationSignatureID = "2023-nov-23-01:07-DEFAULT-PC";
    private static final Logger LOGGER                    = Logger.getLogger();

    // Basic information:
    private String          name;
    private final Biography biography;
    private UnitClass       charClass;
    private long            experience = 0L;
    private int             level      = 1;

    // Attributes:
    @Deprecated(forRemoval = true, since = "Version 0.1")
    private bg.sarakt.characters.attributes1.AttributeMap attributes;


    public PlayerCharacterImpl(String name) {
        this(name, System.currentTimeMillis());
    }

    public PlayerCharacterImpl(String name, long id) {
        super(representationSignatureID, id);
        this.name = name;
        biography = new BiographyImpl(name);
        charClass = new DynamicUnitClass("Dynamic class");
        attributes = new AttributeMapImpl(charClass.getAttributesForLevel(this.level));
        System.out.println(attributes);
    }

    /**
     * @see bg.sarakt.characters.GameCharacter#name()
     */
    @Override
    public String name() {
        return this.name;
    }

    /**
     *
     * @see bg.sarakt.characters.GameCharacter#getLevel()
     */
    @Override
    public int level() {
        return this.level;
    }

    /**
     *
     * @see bg.sarakt.characters.GameCharacter#currentExperience()
     */
    @Override
    public long currentExperience() {
        return this.experience;
    }

    @Override
    public void gainExperience(long newExperience) {
        if (newExperience < 0) {
            LOGGER.debug("Invalid value. Skipping!");
            return;
        }
        if (this.experience >= LevelCalculator.EXPERIENCE_CAP) {
            LOGGER.debug("Experience cap reached");
            this.experience = LevelCalculator.EXPERIENCE_CAP;
            return;
        }

        LOGGER.debug("Character " + name() + " gained " + newExperience + " points of experience");
        this.experience += newExperience;

        // Needs to merge blocks
        if (this.experience > LevelCalculator.EXPERIENCE_CAP) {
            LOGGER.debug("Experience cap reached");
            this.experience = LevelCalculator.EXPERIENCE_CAP;
        }

        int newLevel = LevelCalculator.getInstance().calculateLevel(experience);
        if (this.level >= newLevel) {
            return;
        }
        while (this.level < newLevel) {
            Set<AttributeValuePair> bonuses = charClass.getAttributesForLevel(level);
            // attributes.modifyAttributes(bonuses);
            attributes.setAttributes(bonuses);
            this.level++;
            LOGGER.debug("Level=[" + this.level + "]\t" + attributes);
        }

    }

    /**
     * @see bg.sarakt.characters.GameCharacter#characterClass()
     */
    @Override
    public UnitClass characterClass() {
        return this.charClass;
    }

    @Override
    public AttributeValuePair getAttribute(Attribute attr) {
        return attributes.getAttribute(attr);
    }

    /**
     * @see bg.sarakt.characters.GameCharacter#getAttributeMap()
     */
    @Override
    public AttributeMap getAttributeMap() { return attributes; }

    /**
     * @see bg.sarakt.characters.GameCharacter#getBiography()
     */
    @Override
    public Biography getBiography() { return this.biography; }

    /**
     * @see bg.sarakt.characters.GameCharacter#isAlive()
     */
    @Override
    public boolean isAlive() {
        // AttributeValuePair hitPoints =
        // attributes.getAttribute(Attributes.HIT_POINTS);
        return true;
    }

    /**
     * @see bg.sarakt.characters.GameUnit#prepareForCombat()
     */
    @Override
    public Combatant prepareForCombat() {
        throw new UnsupportedOperationException("Must be implemented");
    }

    /**
     * @see bg.sarakt.characters.GameCharacter#levelUp(bg.sarakt.attributes.levels.Level)
     */
    @Override
    public void levelUp(Level level) {
        throw new UnsupportedOperationException();
    }
}
