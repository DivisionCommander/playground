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

import org.springframework.lang.Nullable;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMap;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifiableAttributeMap;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.characters.Level;

public class AttributeMapImpl implements AttributeMap<Attribute> {

    /** field <code>UNKNOWN_ATTRIBUTE_SUBTYPE</code> */
    private static final String UNKNOWN_ATTRIBUTE_SUBTYPE = "Unknown attribute subtype";
    private static final boolean USE_OLD_LEVELING = false;

    private final ModifiableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry>    primaryMap;
    private final ModifiableAttributeMap<ResourceAttribute, ResourceAttributeEntry> resourceMap;
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
    public AttributeMapImpl(@Nullable Map<PrimaryAttribute, Number> primary, Collection<ResourceAttribute> resources, Collection<SecondaryAttribute> secondary, Level level) {
        primaryMap = new PrimaryAttributeMap(primary, level);
        resourceMap = new ResourceAttributeMap(level, resources, primaryMap);
        secondaryMap = new SecondaryAttributeMap(primaryMap, secondary, level);
        this.level = level;
    }


    public void earnExperience(BigInteger amount) {
        if(level.earnExperience(amount)) {
            levelUp();
        }
    }

    @Override
    public BigDecimal getBaseValue(Attribute attribute) {
        if (attribute instanceof PrimaryAttribute pa) {
            return primaryMap.getBaseValue(pa);
        }
        if (attribute instanceof ResourceAttribute ra) {
            return resourceMap.getBaseValue(ra);
        }
        if (attribute instanceof SecondaryAttribute sa) {
            return secondaryMap.getBaseValue(sa);
        }
        throw new IllegalArgumentException(UNKNOWN_ATTRIBUTE_SUBTYPE);
    }

    /**
     * @see bg.sarakt.attributes.AttributeMap#getAttributeValueForLayer(bg.sarakt.attributes.Attribute,
     *      bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    public BigDecimal getAttributeValueForLayer(Attribute attribute, ModifierLayer layer) {
        if (attribute instanceof PrimaryAttribute pa) {
            return primaryMap.getAttributeValueForLayer(pa, layer);
        }
        if (attribute instanceof ResourceAttribute ra) {
            return resourceMap.getAttributeValueForLayer(ra, layer);
        }
        if (attribute instanceof SecondaryAttribute sa) {
            return secondaryMap.getAttributeValueForLayer(sa, layer);
        }
        throw new IllegalArgumentException(UNKNOWN_ATTRIBUTE_SUBTYPE);
    }

    /**
     * @see bg.sarakt.attributes.AttributeMap#getCurrentAttributeValue(bg.sarakt.attributes.Attribute)
     */
    @Override
    public BigDecimal getCurrentAttributeValue(Attribute attribute) {
        if (attribute instanceof PrimaryAttribute pa) {
            return primaryMap.getCurrentAttributeValue(pa);
        }
        if (attribute instanceof ResourceAttribute ra) {
            return resourceMap.getCurrentAttributeValue(ra);
        }
        if (attribute instanceof SecondaryAttribute sa) {
            return secondaryMap.getCurrentAttributeValue(sa);
        }
        throw new IllegalArgumentException(UNKNOWN_ATTRIBUTE_SUBTYPE);
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
        if (USE_OLD_LEVELING) {
            levelUp_old();
            return;
        }

        Level previous = level.viewPreviousLevel();
        if (previous != null && !previous.getAllModifiers().isEmpty()) {
            applyModifiers(previous.getAllModifiers(), false);
            previous.getAllModifiers().forEach(this::removeModifier);
        }

        Level next = level.viewCurrentLevel();
        if (next != null && !next.getAllModifiers().isEmpty()) {
            next.getPermanentAttributesBonuses().entrySet().forEach(e -> primaryMap.get(e.getKey()).addPermanentBonus(e.getValue()));
            applyModifiers(next.getAllModifiers(), true);
        }
    }

    @Deprecated(forRemoval = true, since = "0.0.6")
    public void levelUp_old() {
        primaryMap.levelUp();
        resourceMap.levelUp();
        secondaryMap.levelUp();
    }

    private void applyModifiers(Collection<AttributeModifier<Attribute>> ams, boolean add) {
        List<AttributeModifier<PrimaryAttribute>> primary = new LinkedList<>();
        List<AttributeModifier<ResourceAttribute>> res = new LinkedList<>();
        List<AttributeModifier<SecondaryAttribute>> sec = new LinkedList<>();
        for (var am : ams) {
            if (am.getAttribute() instanceof PrimaryAttribute pa) {
                primary.add(new AttributeMoDWrapper<>(am, pa));
            }
            if (am.getAttribute() instanceof ResourceAttribute ra) {
                res.add(new AttributeMoDWrapper<>(am, ra));
            }
            if (am.getAttribute() instanceof SecondaryAttribute sa) {
                sec.add(new AttributeMoDWrapper<>(am, sa));
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
        if (am.getAttribute() instanceof PrimaryAttribute pa) {
            primaryMap.removeModifier(new AttributeMoDWrapper<>(am, pa));
        }
        if (am.getAttribute() instanceof ResourceAttribute ra) {
            resourceMap.removeModifier(new AttributeMoDWrapper<>(am, ra));
        }
        if( am.getAttribute() instanceof SecondaryAttribute sa) {
            secondaryMap.removeModifier(new AttributeMoDWrapper<>(am, sa));
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
