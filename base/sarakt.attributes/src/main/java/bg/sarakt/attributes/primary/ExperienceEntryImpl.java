/*
 * ExperienceEntry.java
 *
 * created at 2024-02-04 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.primary;

import static bg.sarakt.attributes.primary.PrimaryAttribute.EXPERIENCE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMap;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.levels.LevelUp;
import bg.sarakt.base.utils.ForRemoval;

import org.springframework.lang.Nullable;

/**
 * Later, it may implement {@link Level} and serves as a proxy to the
 * {@link AttributeMap}s and their {@link AttributeMapEntry}
 * 
 * @since 0.0.13
 */
public final class ExperienceEntryImpl extends PrimaryAttributeEntry implements ExperienceEntry
{
    private Level level;
    
    /**
     * Construct new {@link AttributeMapEntry} for the
     * {@link PrimaryAttribute#EXPERIENCE} with the default level implementation.
     * 
     * @param initialValue
     */
    ExperienceEntryImpl(Number initialValue) {
        this(initialValue, Level.DEFAULT_LEVEL);
    }
    
    public ExperienceEntryImpl(Number initialValue, Level level) {
        super(EXPERIENCE, initialValue);
        this.level = level;
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#gainExperience(java.math.BigInteger)
     */
    @Override
    public LevelUp gainExperience(BigInteger amount) {
        return level.gainExperience(amount);
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#earnExperience(BigInteger)
     * 
     * @deprecated use {@link Level#gainExperience(BigInteger)} instead
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.10")
    public boolean earnExperience(BigInteger amount) {
        addPermanentBonus(amount);
        return level.gainExperience(amount).levelUp();
    }
    
    /**
     * 
     * @see bg.sarakt.attributes.levels.Level#currentExperience()
     */
    @Override
    public BigInteger currentExperience() {
        return level.currentExperience();
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#getLevelNumber()
     */
    @Override
    public Integer getLevelNumber() { return level.getLevelNumber(); }
    
    /**
     * No-Op for now. Later will be implemented with logic according to
     * {@link Level}
     * 
     * @see bg.sarakt.attributes.AttributeMapEntry#levelUp()
     */
    public void levelUp() { /** No-Op for now. NOSONAR **/
    }
    
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#getBaseValue()
     */
    @Override
    public BigDecimal getBaseValue() { return getCurrentValue(); }
    
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#getValueForLayer(bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    public BigDecimal getValueForLayer(ModifierLayer layer) {
        return getCurrentValue();
    }
    
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#getCurrentValue()
     */
    @Override
    public BigDecimal getCurrentValue() { return new BigDecimal(level.currentExperience()); }
    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#recalculate()
     */
    @Override
    public void recalculate() { /** No-Op **/
    
    }
    
    /**
     * Currently only set the level and serves as duplicate of {@link Deprecated}
     * {@link AttributeMapEntry#setLevel(Level)} that will be removed. Later may -
     * or rather will - be repurposed.
     * 
     * @param level
     *            - new level that current {@link ExperienceEntryImpl} would represent.
     * @return this entry or a new one if necessary.
     *            
     * @since 0.0.13
     */
    public ExperienceEntryImpl updateLevelHierarchy(Level level) {
        this.level = level;
        return this;
    }
    
    /**
     * 
     * @see Level#getUnallocatedPoints()
     */
    @Override
    public int getUnallocatedPoints() { return level.getUnallocatedPoints(); }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#experienceThreshold()
     */
    @Override
    public BigInteger experienceThreshold() {
        return level.experienceThreshold();
    }
    /**
     * For now there is no plan to apply any modifiers over the
     * {@link PrimaryAttribute#EXPERIENCE}. However, in later revision some kind of
     * a Rested/Tired system which increase or reduce earn experience may be
     * introduced.
     * 
     * @see bg.sarakt.attributes.internal.AbstractAttributeMapEntry#addModifier(bg.sarakt.attributes.AttributeModifier)
     */
    @Override
    public void addModifier(AttributeModifier<PrimaryAttribute> modifier) { /** No-Op **/
    }
    
    /**
     * For now there is no plan to apply any modifiers over the
     * {@link PrimaryAttribute#EXPERIENCE}. However, in later revision some kind of
     * a Rested/Tired system which increase or reduce earn experience may be
     * introduced.
     * 
     * @see bg.sarakt.attributes.AttributeMapEntry#addModifiers(java.util.Collection)
     */
    @Override
    public void addModifiers(Collection<AttributeModifier<PrimaryAttribute>> modifiers) { /** No-Op **/
    }
    
    /**
     * For now there is no plan to apply any modifiers over the
     * {@link PrimaryAttribute#EXPERIENCE}. However, in later revision some kind of
     * a Rested/Tired system which increase or reduce earn experience may be
     * introduced.
     * 
     * @see bg.sarakt.attributes.AttributeMapEntry#removeModifier(bg.sarakt.attributes.AttributeModifier)
     */
    @Override
    public void removeModifier(AttributeModifier<PrimaryAttribute> modifier) { /** No-Op **/
    }
    
    /**
     * For now there is no plan to apply any modifiers over the
     * {@link PrimaryAttribute#EXPERIENCE}. However, in later revision some kind of
     * a Rested/Tired system which increase or reduce earn experience may be
     * introduced.
     * 
     * @see bg.sarakt.attributes.AttributeMapEntry#removeModifiers(java.util.Collection)
     */
    @Override
    public void removeModifiers(Collection<AttributeModifier<PrimaryAttribute>> modifiers) { /** No-Op **/
    }
    
    /**
     * For now there is no plan to apply any modifiers over the
     * {@link PrimaryAttribute#EXPERIENCE}. However, in later revision some kind of
     * a Rested/Tired system which increase or reduce earn experience may be
     * introduced.
     * 
     * @see bg.sarakt.attributes.AttributeMapEntry#replaceModifier(bg.sarakt.attributes.AttributeModifier,
     *      bg.sarakt.attributes.AttributeModifier)
     */
    @Override
    public void replaceModifier(AttributeModifier<PrimaryAttribute> old, AttributeModifier<PrimaryAttribute> newM) { /** No-Op **/
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + EXPERIENCE.hashCode();
        result = prime * result + Objects.hash(level);
        return result;
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ( !super.equals(obj)) {
            return false;
        }
        if ( !(obj instanceof ExperienceEntryImpl other)) {
            return false;
        }
        return Objects.equals(this.level, other.level);
    }

    /**
     * @see bg.sarakt.attributes.levels.Level#getPermanentBonuses()
     * 
     * @deprecated use {@link LevelNode#getAllModifiers()} filtered by
     *             {@link ModifierType#PRIMARY_PERMANENT}
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.10")
    public Map<PrimaryAttribute, BigInteger> getPermanentBonuses() { return level.getPermanentBonuses(); }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#viewPreviousLevel()
     * 
     * @deprecated Use {@link LevelNode#getPreviousNode()}
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.10")
    public LevelNode viewPreviousLevel() {
        return level.viewPreviousLevel();
    }
    
    /**
     * @see bg.sarakt.attributes.levels.Level#viewCurrentLevel()
     * 
     */
    @Override
    public LevelNode viewCurrentLevel() {return level.viewCurrentLevel();    }

    /**
     * @deprecated Use {@link LevelNode#getNextNode()} instead.
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    @Nullable
    public LevelNode viewNextLevel() {
        return level.viewNextLevel();
    }
    /**
     * @see bg.sarakt.attributes.levels.Level#getAllModifiers()
     */
    @Override
    public List<AttributeModifier<Attribute>> getAllModifiers() { return level.getAllModifiers(); }
}

