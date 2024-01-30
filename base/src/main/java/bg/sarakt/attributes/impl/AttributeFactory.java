/*
 * AttributeFactory.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.base.exceptions.UnknownValueException;
import bg.sarakt.characters.Level;
import bg.sarakt.characters.attributes1.impls.SimpleAttributeFormulaImpl;
import bg.sarakt.logging.Logger;
import bg.sarakt.storing.hibernate.AttributesResourceDAO;
import bg.sarakt.storing.hibernate.AttributesSecondaryDAO;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeEntity;
import bg.sarakt.storing.hibernate.entities.SecondaryAttributeEntity;

@SuppressWarnings("removal")
public final class AttributeFactory implements Attributes {

    private static final Logger LOG = Logger.getLogger();
    private static final AttributeFactory FACTORY     = new AttributeFactory();
    public static AttributeFactory getInstance() { return FACTORY; }

    private static final   String NAME_CURRENT_HIT_POINT   = "Current HP";
    private static final   String NAME_CURRENT_MANA_POINTS = "Current MP";
    private final Map<String, SecondaryAttribute> secondaryAttributes;
    private final Map<String, ResourceAttribute>  resourceAttributes;


    private AttributeFormula getDummyFormula(SecondaryAttribute sa, int level) {

        SimpleAttributeFormulaImpl f  = new SimpleAttributeFormulaImpl("Test");
        f.addAttributeFormula(PrimaryAttribute.STRENGTH, level);
        return f;
    }

    private AttributeFactory() {
        Map<String, SecondaryAttribute> secondary = getSecondaryAttributesFromDB();
        if (secondary == null || secondary.isEmpty()) {
            secondary = defaultSecondaryAttributesMap();
        }
        Map<String, ResourceAttribute> resource = getResourceAttributesFromDB();
        if (resource == null || resource.isEmpty()) {
            resource = defaultResourceAttributesMap();
        }

        secondaryAttributes = Map.copyOf(secondary);
        resourceAttributes = Map.copyOf(resource);
    }

    public  Collection< SecondaryAttribute> defaultSecondaryAttributes() {
        return defaultSecondaryAttributesMap().values();
    }
    private  Map<String, SecondaryAttribute> defaultSecondaryAttributesMap() {
        Map<String, SecondaryAttribute> map = new HashMap<>();

        // AttributeGroup.PHYSICAL:
       map.put(NAME_ACCURACY,        new DummySecondaryAttribute(NAME_ACCURACY,          "ACC", AttributeGroup.PHYSICAL, "", 100));
       map.put(NAME_ATTACK_SKILL,    new DummySecondaryAttribute(NAME_ATTACK_SKILL,      "ATT", AttributeGroup.PHYSICAL, "", 101));
       map.put(NAME_CRITICAL_CHANCE, new DummySecondaryAttribute(NAME_CRITICAL_CHANCE,   "CC", AttributeGroup.PHYSICAL, "", 102));
       map.put(NAME_ATTACK_SPEED,    new DummySecondaryAttribute(NAME_ATTACK_SPEED,      "IAS", AttributeGroup.PHYSICAL, "", 103));
       map.put(NAME_COMBAT_QUICKNESS,new DummySecondaryAttribute(NAME_COMBAT_QUICKNESS,  "CoQ", AttributeGroup.PHYSICAL, "", 104));
       map.put(NAME_HITTING,         new DummySecondaryAttribute(NAME_HITTING,           "HIT", AttributeGroup.PHYSICAL, "", 105));
       map.put(NAME_DEFENCE_RATE,    new DummySecondaryAttribute(NAME_DEFENCE_RATE,      "DEF", AttributeGroup.PHYSICAL, "", 106));
       map.put(NAME_ARMOR_PIERCING,  new DummySecondaryAttribute(NAME_ARMOR_PIERCING,     "ArP", AttributeGroup.PHYSICAL, "", 109));
       map.put(NAME_HIT_RATE,        new DummySecondaryAttribute(NAME_HIT_RATE,          "HRt", AttributeGroup.PHYSICAL, "", 110));
       map.put(NAME_EVADE,           new DummySecondaryAttribute(NAME_EVADE,             "EVA", AttributeGroup.PHYSICAL, "", 111));

        // AttributeGroup.PSYCHICAL:
        map.put(NAME_IQ,        new DummySecondaryAttribute(NAME_IQ,        "IQ", AttributeGroup.PSYCHICAL, "", 200));
        map.put(NAME_CAST_RATE, new DummySecondaryAttribute(NAME_CAST_RATE, "FCR", AttributeGroup.PSYCHICAL, "", 201));
        map.put(NAME_RESISTANCE,new DummySecondaryAttribute(NAME_RESISTANCE, "RES", AttributeGroup.PSYCHICAL, "", 204));
        map.put(NAME_KNOWLEDGE, new DummySecondaryAttribute(NAME_KNOWLEDGE, "KNW", AttributeGroup.PSYCHICAL, "", 205));

        // AttributeGroup.PERSON:
        map.put(NAME_COMBAT_RATING, new DummySecondaryAttribute(NAME_COMBAT_RATING, "CoR", AttributeGroup.PERSON, "", 301));
        return map;
    }

    public Collection<ResourceAttribute> defaultResourceAttributes() {
        return defaultResourceAttributesMap().values();
    }
    private Map<String, ResourceAttribute> defaultResourceAttributesMap() {
        Map<String, ResourceAttribute> map = new HashMap<>();

        map.put(NAME_HIT_POINTS, new DummyResourceAttribute(NAME_HIT_POINTS, "HP", PrimaryAttribute.CONSTITUTION, NAME_CURRENT_HIT_POINT, 107));
        map.put(NAME_MANA_POINTS, new DummyResourceAttribute(NAME_MANA_POINTS, "MP", PrimaryAttribute.INTELLIGENCE, NAME_CURRENT_MANA_POINTS, 202));

        map.put(NAME_ENERGY, new DummyResourceAttribute(NAME_ENERGY, "NGY", PrimaryAttribute.WILL, NAME_ENERGY, 302));
        map.put(NAME_VIGOUR, new DummyResourceAttribute(NAME_VIGOUR, "VIG", PrimaryAttribute.SPIRIT, NAME_VIGOUR, 303));

        return map;
    }

    private Map<String, SecondaryAttribute> getSecondaryAttributesFromDB() {
        try {
            AttributesSecondaryDAO dao = new AttributesSecondaryDAO();
            List<SecondaryAttributeEntity> results = dao.findAll();
            if (results == null || results.isEmpty()) {
                return Collections.emptyMap();
            }
            return results.stream().map(this::mapSecondary).collect(Collectors.toMap(sa -> sa.fullName(), sa -> sa));
        } catch (Exception e) {
            LOG.error("Cannot get data from DB! Reason " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    private Map<String, ResourceAttribute> getResourceAttributesFromDB() {
        try {
            AttributesResourceDAO dao = new AttributesResourceDAO();
            List<ResourceAttributeEntity> results = dao.findAll();
            if (results == null || results.isEmpty()) {
                return Collections.emptyMap();
            }
            return results.stream().map(this::mapResources).collect(Collectors.toMap(ra -> ra.fullName(), ra -> ra));
        } catch (Exception e) {
            LOG.error("Cannot get data from DB! Reason " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    private SecondaryAttribute mapSecondary(SecondaryAttributeEntity e) {
        return new SecondaryAttributeImpl(e);
    }

    private ResourceAttribute mapResources(ResourceAttributeEntity entity) {
        return new ResourceAttributeImpl(entity);
    }

    public Collection<SecondaryAttribute> getSecondaryAttributes() { return secondaryAttributes.values(); }

    public Collection<ResourceAttribute> getResourceAttribute() { return resourceAttributes.values(); }

    private record DummySecondaryAttribute(String fullName, String abbreviation, AttributeGroup group, String description, long getId)
            implements SecondaryAttribute {

        /**
         * @see bg.sarakt.attributes.SecondaryAttribute#getFormula(int)
         */
        @Override
        public AttributeFormula getFormula(int level) {
            return getInstance().getDummyFormula(this, level);
        }

        /**
         * @see bg.sarakt.attributes.SecondaryAttribute#getEntry(bg.sarakt.attributes.impl.PrimaryAttributeMap,
         *      bg.sarakt.characters.Level)
         */
        @Override
        public SecondaryAttributeEntry getEntry(IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primaryAttributes, Level level) {
            return new SecondaryAttributeEntry(this, primaryAttributes, level);
        }
    }

    private record DummyResourceAttribute(String fullName, String abbreviation, PrimaryAttribute getPrimaryAttribute, String description, long getId)
            implements ResourceAttribute {

        /**
         * @see bg.sarakt.attributes.Attribute#group()
         */
        @Override
        public AttributeGroup group() {
            return getPrimaryAttribute.group();
        }

        /**
         * @see bg.sarakt.attributes.ResourceAttribute#getCoefficientForLevel(bg.sarakt.characters.Level)
         */
        @Override
        public BigDecimal getCoefficientForLevel(Level level) {
            return BigDecimal.ONE.multiply(new BigDecimal(level.getLevelNumber()));
        }

        /**
         * @see bg.sarakt.attributes.ResourceAttribute#getEntry(bg.sarakt.attributes.AttributeMapEntry,
         *      bg.sarakt.characters.Level)
         */
        @Override
        public ResourceAttributeEntry getEntry(AttributeMapEntry<PrimaryAttribute> primaryAttributeEntry, Level level) {
            return new ResourceAttributeEntry(this, primaryAttributeEntry, level);
        }

        @Override
        public String toString() {
            return fullName;
        }

    }

    /**
     * @param attribute
     * @return
     */
    public Attribute ofName(String attribute) {
        try {
            return PrimaryAttribute.ofName(attribute);
        } catch (Exception e) {
            // IGNORE seams its not primary attribute
        }
        if (secondaryAttributes.containsKey(attribute)) {
            return secondaryAttributes.get(attribute);
        }
        if (resourceAttributes.containsKey(attribute)) {
            return resourceAttributes.get(attribute);
        }
        throw new UnknownValueException("Unknown or unsupported attribute!");
    }
}
