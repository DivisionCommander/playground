/*
 * AttributeMapImpl.java
 *
 * created at 2024-01-16 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.ModifiableAttributeMap;
import bg.sarakt.attributes.AttributeMap;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.attributes.SecondaryAttributes;
import bg.sarakt.characters.Level;

public class AttributeMapImpl implements AttributeMap<Attribute> {

    private final ModifiableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry>     primaryMap;
    private final ModifiableAttributeMap<SecondaryAttribute, SecondaryAttributeEntry> secondaryMap;

    public AttributeMapImpl() {
        this(Level.TEMP);
    }

    public AttributeMapImpl(Level level) {
        this(new PrimaryAttributeMap(level), SecondaryAttributes.getSecondaryAttributes(), level);
    }

    public AttributeMapImpl(Map<PrimaryAttribute, Number> values, Set<SecondaryAttribute> secondary, Level level) {
        this(new PrimaryAttributeMap(values, level), secondary, level);
    }

    public AttributeMapImpl(PrimaryAttributeMap primary, Set<SecondaryAttribute> secondary, Level level) {
        this.primaryMap = primary;
        this.secondaryMap = new SecondaryAttributeMap(primary, secondary, level);
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
        values.putAll(secondaryMap.getAllValues());

        return values;
    }

    /**
     * @see bg.sarakt.attributes.AttributeMap#levelUp()
     */
    @Override
    public void levelUp() {
        primaryMap.levelUp();
        secondaryMap.levelUp();
    }

    @Override
    public void levelUp(Level level) {
        primaryMap.levelUp(level);
        secondaryMap.levelUp(level);
    }
}
