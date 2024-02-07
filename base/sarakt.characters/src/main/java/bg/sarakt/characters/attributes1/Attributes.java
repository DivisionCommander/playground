/*
 * Attributes.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeGroup;

@Deprecated()
public final class Attributes
{

    private static final String S_ACCURACY          = "Accuracy";
    private static final String S_RESISTANCE        = "Resistance";
    private static final String S_ATTACK_SKILL      = "Attack Skill";
    private static final String S_COMBAT_QUICKNESS  = "Combat Quickness";
    private static final String S_COMBAT_RATING     = "Combat rating";
    private static final String S_IQ                = "IQ";
    private static final String S_ARMOR_PIERCING    = "Armor Piercing";
    private static final String S_ATTACK_SPEED      = "Attack Speed";
    private static final String S_CAST_RATE         = "Cast rate";
    private static final String S_CRITICAL_CHANCE   = "Critical Chance";
    private static final String S_DEFENCE_RATE      = "Defence Rate";
    private static final String S_EVADE             = "Evade";
    private static final String S_HIT_RATE          = "Hit Rate";
    private static final String S_HIT_POINTS        = "Hit Points";
    private static final String S_CURRENT_HIT_POINT = "Current HP";
    private static final String S_HITTING           = "Hitting";

    private static Attribute ACCURACY           = null;// new AttributeImpl(S_ACCURACY, "ACC", "");
    private static Attribute RESISTANCE         = null;// new AttributeImpl(S_RESISTANCE, "RES", "");
    private static Attribute ATTACK_SKILL       = null;// new AttributeImpl(S_ATTACK_SKILL, "ATT", "");
    private static Attribute ATTACK_SPEED       = null;// new AttributeImpl(S_ATTACK_SPEED, "IAS", "");
    private static Attribute COMBAT_QUICKNESS   = null;// new AttributeImpl(S_COMBAT_QUICKNESS, "CoQ", "");
    private static Attribute COMBAT_RATING      = null;// new AttributeImpl(S_COMBAT_RATING, "CoR", "");
    private static Attribute IQ                 = null;// new AttributeImpl(S_IQ, "IQ", "");
    private static Attribute ARMOR_PIERCING     = null;// new AttributeImpl(S_ARMOR_PIERCING, "ArP", "");
    private static Attribute CAST_RATE          = null;// new AttributeImpl(S_CAST_RATE, "FCR", "");
    private static Attribute CRITICAL_CHANCE    = null;// new AttributeImpl(S_CRITICAL_CHANCE, "", "");
    private static Attribute DEFENCE_RATE       = null;// new AttributeImpl(S_DEFENCE_RATE, "DEF", "");
    private static Attribute EVADE              = null;// new AttributeImpl(S_EVADE, "EVA", "");
    private static Attribute HIT_RATE           = null;// new AttributeImpl(S_HIT_RATE, "HRt", "");
    private static Attribute HIT_POINTS         = null;// new AttributeImpl(S_HIT_POINTS, "HP", AttributeGroup.PHYSICAL, "");
    private static Attribute CURRENT_HIT_POINTS = null;// new AttributeImpl(S_CURRENT_HIT_POINT, "cHP", AttributeGroup.PHYSICAL, "");
    private static Attribute HITTING            = null;// new AttributeImpl(S_HITTING, "HIT", "");

    static record AttributeImpl(String fullName, String abbreviation, AttributeGroup group, String description) {// implements Attribute

        public AttributeImpl(String fullName, String abbreviation, String description)
        {
            this(fullName, abbreviation, null, description);
        }

        @Override
        public String toString()
        {
            return fullName;
        }
    }
}
