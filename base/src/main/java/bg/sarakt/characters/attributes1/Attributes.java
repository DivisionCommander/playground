/*
 * Attributes.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.Attribute.AttributeGroup;

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

    public static Attribute ACCURACY           = new AttributeImpl(S_ACCURACY, "ACC", "");
    public static Attribute RESISTANCE         = new AttributeImpl(S_RESISTANCE, "RES", "");
    public static Attribute ATTACK_SKILL       = new AttributeImpl(S_ATTACK_SKILL, "ATT", "");
    public static Attribute ATTACK_SPEED       = new AttributeImpl(S_ATTACK_SPEED, "IAS", "");
    public static Attribute COMBAT_QUICKNESS   = new AttributeImpl(S_COMBAT_QUICKNESS, "CoQ", "");
    public static Attribute COMBAT_RATING      = new AttributeImpl(S_COMBAT_RATING, "CoR", "");
    public static Attribute IQ                 = new AttributeImpl(S_IQ, "IQ", "");
    public static Attribute ARMOR_PIERCING     = new AttributeImpl(S_ARMOR_PIERCING, "ArP", "");
    public static Attribute CAST_RATE          = new AttributeImpl(S_CAST_RATE, "FCR", "");
    public static Attribute CRITICAL_CHANCE    = new AttributeImpl(S_CRITICAL_CHANCE, "", "");
    public static Attribute DEFENCE_RATE       = new AttributeImpl(S_DEFENCE_RATE, "DEF", "");
    public static Attribute EVADE              = new AttributeImpl(S_EVADE, "EVA", "");
    public static Attribute HIT_RATE           = new AttributeImpl(S_HIT_RATE, "HRt", "");
    public static Attribute HIT_POINTS         = new AttributeImpl(S_HIT_POINTS, "HP", AttributeGroup.PHYSICAL, "");
    public static Attribute CURRENT_HIT_POINTS = new AttributeImpl(S_CURRENT_HIT_POINT, "cHP", AttributeGroup.PHYSICAL, "");
    public static Attribute HITTING            = new AttributeImpl(S_HITTING, "HIT", "");

    private static record AttributeImpl(String fullName, String abbreviation, AttributeGroup group, String description) implements Attribute
    {

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
