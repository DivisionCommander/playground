/*
 * SecondaryAttributes.java
 *
 * created at 2024-01-16 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.attributes.Attribute.AttributeGroup;

@Deprecated
public final class SecondaryAttributes {

    private static final SecondaryAttributes INSTANCE = new SecondaryAttributes();

    private static final String N_CURRENT_MANA_POINTS = "Current MP";
    private static final String N_MANA_POINTS         = "Mana Points";
    private static final String N_KNOWLEDGE           = "Knowledge";
    private static final String N_ACCURACY            = "Accuracy";
    private static final String N_RESISTANCE          = "Resistance";
    private static final String N_ATTACK_SKILL        = "Attack Skill";
    private static final String N_COMBAT_QUICKNESS    = "Combat Quickness";
    private static final String N_COMBAT_RATING       = "Combat rating";
    private static final String N_IQ                  = "IQ";
    private static final String N_ARMOR_PIERCING      = "Armor Piercing";
    private static final String N_ATTACK_SPEED        = "Attack Speed";
    private static final String N_CAST_RATE           = "Cast rate";
    private static final String N_CRITICAL_CHANCE     = "Critical Chance";
    private static final String N_DEFENCE_RATE        = "Defence Rate";
    private static final String N_EVADE               = "Evade";
    private static final String N_HIT_RATE            = "Hit Rate";
    private static final String N_HIT_POINTS          = "Hit Points";
    private static final String N_CURRENT_HIT_POINT   = "Current HP";
    private static final String N_HITTING             = "Hitting";

    private final Set<SecondaryAttribute>                              allAttributes;
    private final Map<AttributeRecord, Map<Integer, AttributeFormula>> formulas;

    public Set<SecondaryAttribute> getSecondaryAttributes() { return allAttributes; }

    public AttributeFormula getFormula(AttributeRecord sa, int level) {
        return formulas.get(sa).get(level);
    }

    public static SecondaryAttributes getInstance() { return INSTANCE; }

    {
        Set<AttributeRecord> set = new HashSet<>();
        set.add(new AttributeRecord(N_ACCURACY, "ACC", AttributeGroup.PHYSICAL, "", 100));
        set.add(new AttributeRecord(N_ATTACK_SKILL, "ATT", AttributeGroup.PHYSICAL, "", 101));
        set.add(new AttributeRecord(N_CRITICAL_CHANCE, "CC", AttributeGroup.PHYSICAL, "", 102));
        set.add(new AttributeRecord(N_ATTACK_SPEED, "IAS", AttributeGroup.PHYSICAL, "", 103));
        set.add(new AttributeRecord(N_COMBAT_QUICKNESS, "CoQ", AttributeGroup.PHYSICAL, "", 104));
        set.add(new AttributeRecord(N_HITTING, "HIT", AttributeGroup.PHYSICAL, "", 105));
        set.add(new AttributeRecord(N_DEFENCE_RATE, "DEF", AttributeGroup.PHYSICAL, "", 106));
        set.add(new AttributeRecord(N_HIT_POINTS, "HP", AttributeGroup.PHYSICAL, "", 107));
        set.add(new AttributeRecord(N_CURRENT_HIT_POINT, "cHP", AttributeGroup.PHYSICAL, "", 108));
        set.add(new AttributeRecord(N_ARMOR_PIERCING, "ArP", AttributeGroup.PHYSICAL, "", 109));
        set.add(new AttributeRecord(N_HIT_RATE, "HRt", AttributeGroup.PHYSICAL, "", 110));
        set.add(new AttributeRecord(N_EVADE, "EVA", AttributeGroup.PHYSICAL, "", 111));

        set.add(new AttributeRecord(N_COMBAT_RATING, "CoR", AttributeGroup.PERSON, "", 301));

        set.add(new AttributeRecord(N_IQ, "IQ", AttributeGroup.PSYCHICAL, "", 200));
        set.add(new AttributeRecord(N_CAST_RATE, "FCR", AttributeGroup.PSYCHICAL, "", 201));
        set.add(new AttributeRecord(N_MANA_POINTS, "MP", AttributeGroup.PSYCHICAL, "", 202));
        set.add(new AttributeRecord(N_CURRENT_MANA_POINTS, "cMP", AttributeGroup.PSYCHICAL, "", 203));
        set.add(new AttributeRecord(N_RESISTANCE, "RES", AttributeGroup.PSYCHICAL, "", 204));
        set.add(new AttributeRecord(N_KNOWLEDGE, "KNW", AttributeGroup.PSYCHICAL, "", 205));

        set.stream().forEach(null);

        allAttributes = Set.copyOf(set);
        var map = set.stream().collect(Collectors.toMap(sa -> sa, sa -> new HashMap<Integer, AttributeFormula>()));
        formulas = Map.copyOf(map);
    }

    private record AttributeRecord(String fullName, String abbreviation, AttributeGroup group, String description, long getId)
            implements SecondaryAttribute {

        /**
         * @see bg.sarakt.attributes.SecondaryAttribute#getFormula(int)
         */
        @Override
        public AttributeFormula getFormula(int level) {
            return SecondaryAttributes.getInstance().getFormula(this, level);
        }
    }
}
