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
import java.util.Map;

import org.springframework.lang.Nullable;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMap;
import bg.sarakt.attributes.ModifiableAttributeMap;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.characters.Level;

public class AttributeMapImpl implements AttributeMap<Attribute> {

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

    public AttributeMapImpl(Map<PrimaryAttribute, Number> values, Collection<SecondaryAttribute> secondary, Level level) {
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
        throw new IllegalArgumentException("Unknown attribute subtype");
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
        throw new IllegalArgumentException("Unknown attribute subtype");
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
        throw new IllegalArgumentException("Unknown attribute subtype");
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

    /**
     * @see bg.sarakt.attributes.AttributeMap#levelUp()
     */
    @Override
    public void levelUp() {
        primaryMap.levelUp();
        resourceMap.levelUp();
        secondaryMap.levelUp();
    }

}
