/*
 * AttributeFactory.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.characters.Level;
import bg.sarakt.characters.attributes1.impls.SimpleAttributeFormulaImpl;
import bg.sarakt.storing.hibernate.AttributesResourceDAO;
import bg.sarakt.storing.hibernate.AttributesSecondaryDAO;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeEntity;
import bg.sarakt.storing.hibernate.entities.SecondaryAttributeEntity;

public final class AttributeFactory {

    private static final AttributeFactory FACTORY     = new AttributeFactory();
//    public static final Attribute         PHYSIQUE;
//    public static final Attribute         PSYCHE;
//    public static final Attribute         PERSONALITY;


//    static {
//            PHYSIQUE    = new BaseAttribute("Physique", "PH", AttributeGroup.PHYSICAL, "");
//                PSYCHE      = new BaseAttribute("Psyche", "PS", AttributeGroup.PSYCHICAL,
//                "Ability to continue works under stress.");
//               PERSONALITY = new BaseAttribute("Personality", "PN", AttributeGroup.PERSON,
//                "Sliders that hold information about person");
//
//
//    }
    public static AttributeFactory getInstance() { return FACTORY; }

    private static final String NAME_ACCURACY            = "Accuracy";
    private static final String NAME_ARMOR_PIERCING      = "Armor Piercing";
    private static final String NAME_ATTACK_SKILL        = "Attack Skill";
    private static final String NAME_ATTACK_SPEED        = "Attack Speed";
    private static final String NAME_CAST_RATE           = "Cast rate";
    private static final String NAME_COMBAT_QUICKNESS    = "Combat Quickness";
    private static final String NAME_COMBAT_RATING       = "Combat rating";
    private static final String NAME_CRITICAL_CHANCE     = "Critical Chance";
    private static final String NAME_CURRENT_HIT_POINT   = "Current HP";
    private static final String NAME_CURRENT_MANA_POINTS = "Current MP";
    private static final String NAME_DEFENCE_RATE        = "Defence Rate";
    private static final String NAME_ENERGY              = "Energy";
    private static final String NAME_EVADE               = "Evade";
    private static final String NAME_HIT_POINTS          = "Hit Points";
    private static final String NAME_HIT_RATE            = "Hit Rate";
    private static final String NAME_HITTING             = "Hitting";
    private static final String NAME_IQ                  = "IQ";
    private static final String NAME_KNOWLEDGE           = "Knowledge";
    private static final String NAME_MANA_POINTS         = "Mana Points";
    private static final String NAME_RESISTANCE          = "Resistance";
    private static final String NAME_VIGOUR              = "Vigour";


    private final Set<SecondaryAttribute> secondaryAttributes;
    private final Set<ResourceAttribute>  resourceAttributes;


    private AttributeFormula getDummyFormula(SecondaryAttribute sa, int level) {

        SimpleAttributeFormulaImpl f  = new SimpleAttributeFormulaImpl("Test");
        f.addAttributeFormula(PrimaryAttribute.STRENGTH, level);
        return f;
    }

    private AttributeFactory() {
        Set<SecondaryAttribute> secondary = getSecondaryAttributesFromDB();
        if (secondary == null || secondary.isEmpty()) {
            secondary = defaultSecondaryAttributes();
            Map<SecondaryAttribute, Map<Integer, AttributeFormula>> map = secondary.stream()
                    .collect(Collectors.toMap(sa -> sa, sa -> new HashMap<>()));
        }
        Set<ResourceAttribute> resource = getResourceAttributesFromDB();
        if (resource == null || resource.isEmpty()) {
            resource = defaultResourceAttributes();
        }

        secondaryAttributes = Set.copyOf(secondary);
        resourceAttributes = Set.copyOf(resource);
    }

    public  Set<SecondaryAttribute> defaultSecondaryAttributes() {
        Set<SecondaryAttribute> set = new HashSet<>();

        // AttributeGroup.PHYSICAL:
        set.add(new DummySecondaryAttribute(NAME_ACCURACY, "ACC", AttributeGroup.PHYSICAL, "", 100));
        set.add(new DummySecondaryAttribute(NAME_ATTACK_SKILL, "ATT", AttributeGroup.PHYSICAL, "", 101));
        set.add(new DummySecondaryAttribute(NAME_CRITICAL_CHANCE, "CC", AttributeGroup.PHYSICAL, "", 102));
        set.add(new DummySecondaryAttribute(NAME_ATTACK_SPEED, "IAS", AttributeGroup.PHYSICAL, "", 103));
        set.add(new DummySecondaryAttribute(NAME_COMBAT_QUICKNESS, "CoQ", AttributeGroup.PHYSICAL, "", 104));
        set.add(new DummySecondaryAttribute(NAME_HITTING, "HIT", AttributeGroup.PHYSICAL, "", 105));
        set.add(new DummySecondaryAttribute(NAME_DEFENCE_RATE, "DEF", AttributeGroup.PHYSICAL, "", 106));
        // set.add(new DummySecondaryAttributeRecord(NAME_HIT_POINTS, "HP",
        // AttributeGroup.PHYSICAL, "", 107));
        // set.add(new DummySecondaryAttributeRecord(NAME_CURRENT_HIT_POINT, "cHP",
        // AttributeGroup.PHYSICAL, "", 108));
        set.add(new DummySecondaryAttribute(NAME_ARMOR_PIERCING, "ArP", AttributeGroup.PHYSICAL, "", 109));
        set.add(new DummySecondaryAttribute(NAME_HIT_RATE, "HRt", AttributeGroup.PHYSICAL, "", 110));
        set.add(new DummySecondaryAttribute(NAME_EVADE, "EVA", AttributeGroup.PHYSICAL, "", 111));

        // AttributeGroup.PSYCHICAL:
        set.add(new DummySecondaryAttribute(NAME_IQ, "IQ", AttributeGroup.PSYCHICAL, "", 200));
        set.add(new DummySecondaryAttribute(NAME_CAST_RATE, "FCR", AttributeGroup.PSYCHICAL, "", 201));
        // set.add(new DummySecondaryAttributeRecord(NAME_MANA_POINTS, "MP",
        // AttributeGroup.PSYCHICAL, "", 202));
        // set.add(new DummySecondaryAttributeRecord(NAME_CURRENT_MANA_POINTS, "cMP",
        // AttributeGroup.PSYCHICAL, "", 203));
        set.add(new DummySecondaryAttribute(NAME_RESISTANCE, "RES", AttributeGroup.PSYCHICAL, "", 204));
        set.add(new DummySecondaryAttribute(NAME_KNOWLEDGE, "KNW", AttributeGroup.PSYCHICAL, "", 205));

        // AttributeGroup.PERSON:
        set.add(new DummySecondaryAttribute(NAME_COMBAT_RATING, "CoR", AttributeGroup.PERSON, "", 301));
        return set;
    }

    public Set<ResourceAttribute> defaultResourceAttributes() {
        Set<ResourceAttribute> set = new HashSet<>();

        set.add(new DummyResourceAttribute(NAME_HIT_POINTS, "HP", PrimaryAttribute.CONSTITUTION, NAME_CURRENT_HIT_POINT, 107));
        set.add(new DummyResourceAttribute(NAME_MANA_POINTS, "MP", PrimaryAttribute.INTELLIGENCE, NAME_CURRENT_MANA_POINTS, 202));

        set.add(new DummyResourceAttribute(NAME_ENERGY, "NGY", PrimaryAttribute.WILL, NAME_ENERGY, 302));
        set.add(new DummyResourceAttribute(NAME_VIGOUR, "VIG", PrimaryAttribute.SPIRIT, NAME_VIGOUR, 303));


        return set;
    }

    private Set<SecondaryAttribute> getSecondaryAttributesFromDB() {
        try {
            AttributesSecondaryDAO dao = new AttributesSecondaryDAO();
            List<SecondaryAttributeEntity> results = dao.findAll();
            if(results== null || results.isEmpty()) {
                return Collections.emptySet();
            }
            return results.stream().map(this::mapSecondary).collect(Collectors.toSet());
        } catch (Exception e) {
            // FIXME: better handle;
            System.out.println("SOMETHING GOES WRONG.");
            e.printStackTrace();
            return Collections.emptySet();
        }
    }

    private Set<ResourceAttribute> getResourceAttributesFromDB() {
        try {
            AttributesResourceDAO dao = new AttributesResourceDAO();
            List<ResourceAttributeEntity> results = dao.findAll();
            if(results== null || results.isEmpty()) {
                return Collections.emptySet();
            }
            return results.stream().map(this::mapResources).collect(Collectors.toSet());
        } catch (Exception e) {
            // FIXME: better handle;
            System.out.println("SOMETHING GOES WRONG.");
            e.printStackTrace();
            return Collections.emptySet();
        }
    }

    private SecondaryAttribute mapSecondary(SecondaryAttributeEntity e) {
        return new SecondaryAttributeImpl(e);
    }

    private ResourceAttribute mapResources(ResourceAttributeEntity entity) {
        return new ResourceAttributeImpl(entity);
    }

    public Set<SecondaryAttribute> getSecondaryAttributes() { return secondaryAttributes; }

    public Set<ResourceAttribute> getResourceAttribute() { return resourceAttributes; }

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
            // TODO Auto-generated method stub
            return fullName;
        }

    }

}
