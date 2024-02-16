/*
 * PlayerCharacterImpl.java
 *
 * created at 2023-11-23 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.impls;

import java.math.BigDecimal;
import java.math.BigInteger;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.impl.AttributeMapImpl;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.primary.PrimaryAttributeMap;
import bg.sarakt.base.AbstractGameObject;
import bg.sarakt.base.utils.ForRemoval;
import bg.sarakt.characters.Biography;
import bg.sarakt.characters.GameCharacter;
import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.characters.attributes1.AttributeMap;
import bg.sarakt.characters.attributes1.UnitClass;
import bg.sarakt.characters.attributes1.impls.DynamicUnitClass;
import bg.sarakt.characters.utils.CharacterUtils;
import bg.sarakt.combats.Combatant;
import bg.sarakt.logging.Logger;

public class PlayerCharacterImpl extends AbstractGameObject implements GameCharacter {
    
    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202311230107L;
    
    private static final String representationSignatureID = "2023-nov-23-01:07-DEFAULT-PC";
    private static final Logger LOGGER                    = Logger.getLogger();
    
    // Basic information:
    private final Biography biography;
    private UnitClass       charClass;
    
    // Attributes:
    private CharacterAttributeMap                         attributeMap;
    
    public PlayerCharacterImpl(String name) {
        this(name, System.currentTimeMillis());
    }
    
    public PlayerCharacterImpl(String name, long id) {
        super(representationSignatureID, id);
        // FIXME ASAP: real data; maybe from constructor.
        attributeMap = new AttributeMapImpl(new PrimaryAttributeMap(), null, null, null);
        
        biography = new BiographyImpl(name);
        charClass = new DynamicUnitClass("Dynamic class");
    }
    
    /**
     * @see bg.sarakt.characters.GameCharacter#name()
     */
    @Override
    public String name() {
        return biography.getCharacterName();
    }
    /**
     *
     * @see bg.sarakt.characters.GameCharacter#getLevelNumber()
     */
    @Override
    public int level() {
        return attributeMap.getLevelNumber();
    }
    
    /**
     *
     * @see bg.sarakt.characters.GameCharacter#currentExperience()
     */
    @Override
    public long currentExperience() {
        // TODO: expose quick access of experience in CharacterAttributeMap
        return attributeMap.getCurrentAttributeValue(PrimaryAttribute.EXPERIENCE).longValue();
    }
    
    /**
     * @deprecated use {@link GameCharacter#earnExperience(BigInteger)} instead.
     */
    @Override
    @Deprecated(since = "0.0.3", forRemoval = true)
    @ForRemoval(since = "0.0.3", expectedRemovalVersion = "0.0.5")
    public void gainExperience(long newExperience) {
        earnExperience(BigInteger.valueOf(newExperience));
    }
    
    /**
     * @see bg.sarakt.characters.GameCharacter#earnExperience(java.math.BigInteger)
     */
    @Override
    public void earnExperience(BigInteger amount) {
        if (amount.signum() == 0) {
            LOGGER.debug("No experience");
            return;
        }
        
        if (amount.signum() < 0) {
            LOGGER.debug("Invalid value. Skipping!");
            return;
        }
        attributeMap.earnExperience(amount);
        LOGGER.debug("Character " + name() + " gained " + amount + " points of experience");
    }
    
    /**
     * @see bg.sarakt.characters.GameCharacter#characterClass()
     */
    @Override
    public UnitClass characterClass() {
        return this.charClass;
    }
    
    /**
     * 
     * @see bg.sarakt.characters.GameCharacter#getAttribute(bg.sarakt.attributes.Attribute)
     * 
     * @deprecated
     */
    @Override
    @Deprecated(forRemoval = true)
    @ForRemoval(expectedRemovalVersion = "0.0.5", description = "Move to the new implementation")
    public AttributeValuePair getAttribute(Attribute attr) {
        BigDecimal value = attributeMap.getCurrentAttributeValue(attr);
        return new AttributeValuePair(attr, value.intValue());
    }
    
    /**
     * @see bg.sarakt.characters.GameCharacter#getAttributeValue(bg.sarakt.attributes.Attribute)
     */
    @Override
    public BigDecimal getAttributeValue(Attribute attr) {
        return attributeMap.getCurrentAttributeValue(attr);
    }
    
    /**
     * @see bg.sarakt.characters.GameCharacter#getAttributeMap()
     * @deprecated
     */
    @Override
    @Deprecated(forRemoval = true)
    @ForRemoval(expectedRemovalVersion = "0.0.5", description = "Move to the new implementation")
    public AttributeMap getAttributeMap() { return CharacterUtils.toOldAttributeMap(attributeMap); }
    
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
     * 
     * @deprecated
     */
    @Override
    @Deprecated(forRemoval = true)
    @ForRemoval(expectedRemovalVersion = "0.0.5", description = "Move to the new implementation")
    public void levelUp(Level level) {
        throw new UnsupportedOperationException();
    }
}
