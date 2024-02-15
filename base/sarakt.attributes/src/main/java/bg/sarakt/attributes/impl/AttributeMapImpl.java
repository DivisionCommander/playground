/*
 * AttributeMapImpl.java
 *
 * created at 2024-01-16 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.ModifiableAttributeMap;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.levels.LevelUp;
import bg.sarakt.attributes.primary.ExperienceEntryImpl;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.primary.PrimaryAttributeEntry;
import bg.sarakt.attributes.primary.PrimaryAttributeMap;
import bg.sarakt.attributes.resources.ResourceAttribute;
import bg.sarakt.attributes.resources.ResourceAttributeEntry;
import bg.sarakt.attributes.resources.ResourceAttributeMap;
import bg.sarakt.attributes.secondary.SecondaryAttribute;
import bg.sarakt.attributes.secondary.SecondaryAttributeEntry;
import bg.sarakt.attributes.secondary.SecondaryAttributeMap;
import bg.sarakt.base.exceptions.UnsupportedSubtypeException;
import bg.sarakt.base.utils.ForRemoval;
import bg.sarakt.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;

public class AttributeMapImpl implements CharacterAttributeMap{
    
    /** field <code>ADD_MODIFIERS</code> */
    private static final boolean ADD_MODIFIERS = true;
    /** field <code>REMOVE_MODIFIERS</code> */
    private static final boolean REMOVE_MODIFIERS = false;
    
    @Value("${use.old.levelling.system:false}")
    private boolean useOldLevelling = false;
    
    private static final Logger LOG = Logger.getLogger();
    
    /** field <code>UNKNOWN_ATTRIBUTE_SUBTYPE</code> */
    private static final String UNKNOWN_ATTRIBUTE_SUBTYPE_MESSAGE = "Unknown attribute subtype";
    
    private final ModifiableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry>     primaryMap;
    private final ModifiableAttributeMap<ResourceAttribute, ResourceAttributeEntry>   resourceMap;
    private final ModifiableAttributeMap<SecondaryAttribute, SecondaryAttributeEntry> secondaryMap;
    private final AtomicInteger                                                       unallocatedPoints;
    
    private ExperienceEntryImpl experience;

    public AttributeMapImpl(@Nullable Map<PrimaryAttribute, Number> primary, Collection<ResourceAttribute> resources,
            Collection<SecondaryAttribute> secondary, Level level) {
        this(new PrimaryAttributeMap(level, primary), resources, secondary, level);
    }
    
    /**
     *
     * @param primary if it is null, a default values Will be used.
     * @param resources
     * @param secondary
     * @param level
     */
    public AttributeMapImpl(PrimaryAttributeMap map, Collection<ResourceAttribute> resources,
            Collection<SecondaryAttribute> secondary, Level level) {
        // PrimaryAttributeMap map = new PrimaryAttributeMap(level, primary);
        this.primaryMap = map;
        this.experience = map.getExperienceEntry();
        this.unallocatedPoints = new AtomicInteger(0);
        resourceMap = new ResourceAttributeMap(primaryMap, resources);
        secondaryMap = new SecondaryAttributeMap(primaryMap, secondary);
        setPoints();
        recalculate();
    }
    
    /**
     * 
     * @see bg.sarakt.attributes.CharacterAttributeMap#getLevelNumber()
     */
    @Override
    public int getLevelNumber() { return this.experience().getLevelNumber(); }
    
    @SuppressWarnings("unchecked")
    public <A extends Attribute, E extends AttributeMapEntry<A>> E get(A attribute)
    {
        switch (attribute)
        {
        case PrimaryAttribute pa:
            return (E) primaryMap.get(pa);
        case ResourceAttribute ra:
            return (E) resourceMap.get(ra);
        case SecondaryAttribute sa:
            return (E) secondaryMap.get(sa);
        default:
            break;
        }
        return null;
    }
    
    private ExperienceEntryImpl experience() {
        return this.experience;
    }

    /**
     * @see bg.sarakt.attributes.CharacterAttributeMap#addPermanentBonus(bg.sarakt.attributes.primary.PrimaryAttribute,
     *      java.math.BigInteger)
     */
    @Override
    public void addPermanentBonus(PrimaryAttribute pa, BigInteger value) {
        primaryMap.get(pa).addPermanentBonus(value);
        recalculate();
    }
    
    /**
     * @see bg.sarakt.attributes.CharacterAttributeMap#unallocatedPoints()
     */
    @Override
    public int unallocatedPoints() {
        return unallocatedPoints.get();
    }
    
    /**
     * @see bg.sarakt.attributes.CharacterAttributeMap#spendUnallocatedPoints(bg.sarakt.attributes.primary.PrimaryAttribute,
     *      java.math.BigInteger)
     */
    @Override
    public void spendUnallocatedPoints(PrimaryAttribute pa, BigInteger value) {
        if (unallocatedPoints() < value.intValue()) {
            throw new IllegalArgumentException("Not enought points to spend!");
        }
        addPermanentBonus(pa, value);
        unallocatedPoints.addAndGet(value.negate().intValue());
    }
    
    @Override
    public void earnExperience(BigInteger amount) {
        if (useOldLevelling) {
            if (experience().gainExperience(amount).levelUp()) {
                doLevelUp();
            }
            return;
        }
        
        LevelUp lvl = experience().gainExperience(amount);
        levelUp(lvl);
    }
    
    private void levelUp(LevelUp lvl) {
        if ( !lvl.levelUp()) {
            return;
        }
        BigInteger points = lvl.unallocatedPoints();
        unallocatedPoints.addAndGet(points.intValue());
        applyModifiers(lvl.toRemove(), REMOVE_MODIFIERS);
        applyModifiers(lvl.toAdd(), ADD_MODIFIERS);
        recalculate();
    }
    
    @Override
    public BigDecimal getBaseValue(Attribute attribute) {
        switch (attribute)
        {
        case PrimaryAttribute pa:
            return primaryMap.getBaseValue(pa);
        case ResourceAttribute ra:
            return resourceMap.getBaseValue(ra);
        case SecondaryAttribute sa:
            return secondaryMap.getBaseValue(sa);
        default:
            throw new IllegalArgumentException(UNKNOWN_ATTRIBUTE_SUBTYPE_MESSAGE);
        }
    }

    /**
     * @see bg.sarakt.attributes.AttributeMap#getAttributeValueForLayer(bg.sarakt.attributes.Attribute,
     *      bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    public BigDecimal getAttributeValueForLayer(Attribute attribute, ModifierLayer layer) {
        switch (attribute)
        {
        case PrimaryAttribute pa:
            return primaryMap.getAttributeValueForLayer(pa, layer);
        case ResourceAttribute ra:
            return resourceMap.getAttributeValueForLayer(ra, layer);
        case SecondaryAttribute sa:
            return secondaryMap.getAttributeValueForLayer(sa, layer);
        default:
            throw new UnsupportedSubtypeException(attribute.getClass(), UNKNOWN_ATTRIBUTE_SUBTYPE_MESSAGE);
        }
    }

    /**
     * @see bg.sarakt.attributes.AttributeMap#getCurrentAttributeValue(bg.sarakt.attributes.Attribute)
     */
    @Override
    public BigDecimal getCurrentAttributeValue(Attribute attribute) {
        switch (attribute)
        {
        case PrimaryAttribute pa:
            return primaryMap.getCurrentAttributeValue(pa);
        case ResourceAttribute ra:
            return resourceMap.getCurrentAttributeValue(ra);
        case SecondaryAttribute sa:
            return secondaryMap.getCurrentAttributeValue(sa);
        default:
            throw new UnsupportedSubtypeException(attribute.getClass(), UNKNOWN_ATTRIBUTE_SUBTYPE_MESSAGE);
        }
    }

    /**
     * @see bg.sarakt.attributes.AttributeMap#getAllValues()
     */
    @Override
    public Map<Attribute, BigDecimal> getAllValues() {
        Map<Attribute, BigDecimal> values = new HashMap<>();

        values.putAll(primaryMap.getAllValues());
        values.putAll(resourceMap.getAllValues());
        values.putAll(secondaryMap.getAllValues());

        return values;
    }

    @Deprecated(forRemoval = true)
    @ForRemoval(expectedRemovalVersion = "0.1.1")
    public void levelUp() {
        doLevelUp();
    }
    
    /**
     * @deprecated This way uses the old leveling system that become deprecated in
     *             version 0.1.x and will be perish.
     *             
     */
    @Deprecated(since = "0.1.0-ALPHA")
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    private void doLevelUp() {
        LOG.debug("Levelling using the old levelling system with is now obsolete.");

        LevelNode previous = experience().viewPreviousLevel();
        if (previous != null && !previous.getAllModifiers().isEmpty()) {
            applyModifiers(previous.getAllModifiers(), REMOVE_MODIFIERS);
            previous.getAllModifiers().forEach(this::removeModifier);
        }

        LevelNode next = experience().viewCurrentLevel();
        if (next != null) {
            if ( !next.getAllModifiers().isEmpty()) {
                applyModifiers(next.getAllModifiers(), ADD_MODIFIERS);
            }
            else {
                recalculate();
            }
        }
    }

    private void recalculate() {
        primaryMap.forEach(PrimaryAttributeEntry::recalculate);
        resourceMap.forEach(ResourceAttributeEntry::recalculate);
        secondaryMap.forEach(SecondaryAttributeEntry::recalculate);
    }

    private void applyModifiers(Collection<AttributeModifier<Attribute>> modifiers, boolean add) {
        List<AttributeModifier<PrimaryAttribute>> primary = new LinkedList<>();
        List<AttributeModifier<ResourceAttribute>> res = new LinkedList<>();
        List<AttributeModifier<SecondaryAttribute>> sec = new LinkedList<>();
        for (var modifier : modifiers) {
            switch (modifier.getAttribute())
            {
            case PrimaryAttribute pa:
                primary.add(new AttributeMoDWrapper<>(modifier, pa));
                break;
            case ResourceAttribute ra:
                res.add(new AttributeMoDWrapper<>(modifier, ra));
                break;
            case SecondaryAttribute sa:
                sec.add(new AttributeMoDWrapper<>(modifier, sa));
                break;
            default:
                Logger.getLogger().error(UNKNOWN_ATTRIBUTE_SUBTYPE_MESSAGE + "\t" + modifier.getAttribute().getClass());
                break;
            }
        }
        if (add) {
            primaryMap.addModifiers(primary);
            resourceMap.addModifiers(res);
            secondaryMap.addModifiers(sec);
        } else {
            primaryMap.removeModifiers(primary);
            resourceMap.removeModifiers(res);
            secondaryMap.removeModifiers(sec);
        }

    }

    private void removeModifier(AttributeModifier<Attribute> am) {
        switch (am.getAttribute())
        {
        case PrimaryAttribute pa:
            primaryMap.removeModifier(new AttributeMoDWrapper<>(am, pa));
            break;
        case ResourceAttribute ra:
            resourceMap.removeModifier(new AttributeMoDWrapper<>(am, ra));
            break;
        case SecondaryAttribute sa:
            secondaryMap.removeModifier(new AttributeMoDWrapper<>(am, sa));
            break;
        
        default:
            Logger.getLogger().error(UNKNOWN_ATTRIBUTE_SUBTYPE_MESSAGE + "\t" + am.getAttribute().getClass());
            break;
        }
    }
    
    private void setPoints() {
        int points = experience().getUnallocatedPoints();
        applyModifiers(experience.getAllModifiers(), ADD_MODIFIERS);
        unallocatedPoints.addAndGet(points);
    }

    private class AttributeMoDWrapper<A extends Attribute> implements AttributeModifier<A> {

        private final AttributeModifier<?> mod;
        private final A                    attribute;

        private AttributeMoDWrapper(AttributeModifier<?> modifier, A attr) {
            this.mod = modifier;
            this.attribute = attr;
        }

        /**
         * @see bg.sarakt.attributes.AttributeModifier#getAttribute()
         */
        @Override
        public A getAttribute() { return attribute; }

        /**
         * @see bg.sarakt.attributes.AttributeModifier#getValue()
         */
        @Override
        public BigDecimal getValue() { return mod.getValue(); }

        /**
         * @see bg.sarakt.attributes.AttributeModifier#getBonusType()
         */
        @Override
        public ModifierType getBonusType() { return mod.getBonusType(); }

        /**
         * @see bg.sarakt.attributes.AttributeModifier#getLayer()
         */
        @Override
        public ModifierLayer getLayer() { return mod.getLayer(); }
        
        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "AttributeMoDWrapper [mod=" + this.mod.getValue() + ", attribute=" + this.attribute + "]";
        }
    }
}
