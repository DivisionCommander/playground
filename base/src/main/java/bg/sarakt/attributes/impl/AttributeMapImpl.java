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

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.ModifiableAttributeMap;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.base.exceptions.UnsupportedSubtypeException;
import bg.sarakt.logging.Logger;

import org.springframework.lang.Nullable;

public class AttributeMapImpl implements CharacterAttributeMap{

    /** field <code>UNKNOWN_ATTRIBUTE_SUBTYPE</code> */
    private static final String UNKNOWN_ATTRIBUTE_SUBTYPE = "Unknown attribute subtype";
    private static final boolean USE_OLD_LEVELING          = false;
    
    private final ModifiableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry>     primaryMap;
    private final ModifiableAttributeMap<ResourceAttribute, ResourceAttributeEntry>   resourceMap;
    private final ModifiableAttributeMap<SecondaryAttribute, SecondaryAttributeEntry> secondaryMap;

    private final Level level;

    public AttributeMapImpl() {
        this(Level.TEMP);
    }

    public AttributeMapImpl(Level level) {
        this(null, AttributeFactory.getInstance().getSecondaryAttributes(), level);
    }

    /**
    *
    * @param primary if it is null, a default values Will be used.
    * @param resources
    * @param secondary
    * @param level
    */
    public AttributeMapImpl(@Nullable Map<PrimaryAttribute, Number> values, Collection<SecondaryAttribute> secondary, Level level) {
        this(values, AttributeFactory.getInstance().getResourceAttribute(), secondary, level);
    }

    /**
     *
     * @param primary if it is null, a default values Will be used.
     * @param resources
     * @param secondary
     * @param level
     */
    public AttributeMapImpl(@Nullable Map<PrimaryAttribute, Number> primary, Collection<ResourceAttribute> resources,
            Collection<SecondaryAttribute> secondary, Level level) {
        primaryMap = new PrimaryAttributeMap(primary).setLevel(level);
        resourceMap = new ResourceAttributeMap(primaryMap, resources).setLevel(level);
        secondaryMap = new SecondaryAttributeMap(primaryMap, secondary).setLevel(level);
        this.level = level;
        recalculate();
    }
    
    public int getLevel() { return this.level.getLevelNumber(); }


    @Override
    public void earnExperience(BigInteger amount) {
        if(level.earnExperience(amount)) {
            levelUp();
        }
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
            throw new IllegalArgumentException(UNKNOWN_ATTRIBUTE_SUBTYPE);
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
            throw new UnsupportedSubtypeException(attribute.getClass(), UNKNOWN_ATTRIBUTE_SUBTYPE);
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
            throw new UnsupportedSubtypeException(attribute.getClass(), UNKNOWN_ATTRIBUTE_SUBTYPE);
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

    @Override
    public void levelUp() {
        System.out.println("Level Up");
        if (USE_OLD_LEVELING) {
            levelUp_old();
            System.out.println(USE_OLD_LEVELING);
            return;
        }

        LevelNode previous = level.viewPreviousLevel();
        if (previous != null && !previous.getAllModifiers().isEmpty()) {
            applyModifiers(previous.getAllModifiers(), false);
            previous.getAllModifiers().forEach(this::removeModifier);
        }

        LevelNode next = level.viewCurrentLevel();
        if (next != null) {
            next.getPermanentBonuses().entrySet().forEach(e -> primaryMap.get(e.getKey()).addPermanentBonus(e.getValue()));
            if ( !next.getAllModifiers().isEmpty()) {
                applyModifiers(next.getAllModifiers(), true);
            }
            else {
                recalculate();
            }
        }
    }

    @Deprecated(forRemoval = true, since = "0.0.6")
    public void levelUp_old() {
        primaryMap.levelUp();
        resourceMap.levelUp();
        secondaryMap.levelUp();
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
                Logger.getLogger().error(UNKNOWN_ATTRIBUTE_SUBTYPE + "\t" + modifier.getAttribute().getClass());
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
            Logger.getLogger().error(UNKNOWN_ATTRIBUTE_SUBTYPE + "\t" + am.getAttribute().getClass());
            break;
        }
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
    }
}
