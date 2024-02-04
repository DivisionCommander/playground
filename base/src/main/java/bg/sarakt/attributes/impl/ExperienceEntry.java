/*
 * ExperienceEntry.java
 *
 * created at 2024-02-04 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.levels.Level;

public final class ExperienceEntry extends PrimaryAttributeEntry {
    
    private Level level;
    
    ExperienceEntry(Number initialValue) {
        super(PrimaryAttribute.EXPERIENCE, initialValue);
    }
    
    /**
     *
     * @deprecated dropping support of {@link Level} and
     *             {@link bg.sarakt.characters.Level} as now
     *             {@link CharacterAttributeMap} would manage leveling of
     *             {@link Attribute}s and their {@link AttributeMapEntry}
     */
    @Deprecated(forRemoval = true, since = "0.0.7")
    ExperienceEntry(Number initialValue, Level level) {
        super(PrimaryAttribute.EXPERIENCE, initialValue, level);
        this.level = level;
    }
    
    boolean earnExperience(BigInteger amount) {
        addPermanentBonus(amount);
        return level.earnExperience(amount);
    }

    BigDecimal getValue() { return new BigDecimal(level.currentExperience()); }
    
    public int currentLevel() {
        return level.getLevelNumber();
    }
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#addModifier(bg.sarakt.attributes.AttributeModifier)
     */
    @Override
    public void addModifier(AttributeModifier<PrimaryAttribute> modifier) { /** No-Op **/
    }
    
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#addModifiers(java.util.Collection)
     */
    @Override
    public void addModifiers(Collection<AttributeModifier<PrimaryAttribute>> modifiers) { /** No-Op **/
    }
    
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#removeModifier(bg.sarakt.attributes.AttributeModifier)
     */
    @Override
    public void removeModifier(AttributeModifier<PrimaryAttribute> modifier) { /** No-Op **/
    }
    
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#removeModifiers(java.util.Collection)
     */
    @Override
    public void removeModifiers(Collection<AttributeModifier<PrimaryAttribute>> modifiers) { /** No-Op **/
    }
    
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#replaceModifier(bg.sarakt.attributes.AttributeModifier,
     *      bg.sarakt.attributes.AttributeModifier)
     */
    @Override
    public void replaceModifier(AttributeModifier<PrimaryAttribute> old, AttributeModifier<PrimaryAttribute> newM) { /** No-Op **/
    }
    
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#levelUp()
     */
    @Override
    public void levelUp() { /** No-Op **/
    }
    
    
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#getBaseValue()
     */
    @Override
    public BigDecimal getBaseValue() { return getValue(); }
    
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#getValueForLayer(bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    public BigDecimal getValueForLayer(ModifierLayer layer) {
        return getValue();
    }
    
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#getCurrentValue()
     */
    // @Override
    @Override
    public BigDecimal getCurrentValue() { return getValue(); }
    
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#setLevel(bg.sarakt.attributes.levels.Level)
     */
    @Override
    public AttributeMapEntry<PrimaryAttribute> setLevel(Level level) {
        this.level = level;
        return this;
    }
    
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#recalculate()
     */
    @Override
    public void recalculate() { /** No-Op **/
        
    }
    
    public void injectLevel(Level level) {
        this.level = level;
    }
}
