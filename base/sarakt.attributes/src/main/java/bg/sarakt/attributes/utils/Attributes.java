/*
 * Attributes.java
 *
 * created at 2024-01-29 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.utils;

/**
 * Manually sort members and split them into groups... somehow
 */
public interface Attributes {
    
    // Group Primary
    String NAME_STRENGTH     = "Strength";
    String ABBR_STRENGTH     = "STR";
    String DESC_STRENGTH     = "Basic physical strangth. Affect force attacks and (gear) lifting and carring capacities.";
    String NAME_AGILITY      = "Fine Motoric";
    String ABBR_AGILITY      = "FMS";
    String DESC_AGILITY      = "Basic fine motoric skills. Affects delicate, fast and multi-movement attacks.";
    String NAME_CONSTITUTION = "Stamina";
    String ABBR_CONSTITUTION = "STA";
    String DESC_CONSTITUTION = "Basic ability to take damage without die.";
    String NAME_WISDOM       = "Wisdom";
    String ABBR_WISDOM       = "WIS";
    String DESC_WISDOM       = "Identify thing, Understand more complicated spells";
    String NAME_WILL         = "Will Power";
    String ABBR_WILL         = "WILL";
    String DESC_WILL         = "Projecting will over another character of protect from influence from person, artifacts and etc.";
    String ABBR_INTELLIGENCE = "INT";
    String ABBR_KNOWLEDGE    = "KNW";
    String ABBR_PSIONIC      = "PSI";
    String ABBR_SPIRIT       = "SPI";
    String NAME_XP = "Experience";
    String ABBR_XP = "XP";
    String DESC_XP = "Amount of experience character has";
    
    // Group Resource
    String ABBR_VIGOUR = "VIG";
    String ABBR_ENERGY = "NGY";
    String ABBR_LIFE   = "HP";
    String ABBR_MANA   = "MP";
    
    // Group Secondary
    String ABBR_ACCURACY         = "ACC";
    String ABBR_ARMOR_PIERCING   = "ArP";
    String DESC_ARMOR_PIERCING   = "Armor Piercing";
    String ABBR_ATTACK_SKILL     = "ATT";
    String ABBR_COMBAT_QUICKNESS = "CoQ";
    String ABBR_ATTACK_SPEED     = "IAS";
    String ABBR_CAST_RATE        = "CAST";
    String ABBR_CHAR             = "CHAR";
    String DESC_CHAR             = "Ability to affect and infuluence othe creatures, critters and beings at all";
    String ABBR_COMBAT_RATING    = "CoR";
    String ABBR_CRITICAL_CHANCE  = "CC";
    String ABBR_DEFENCE_RATE     = "DEF";
    String ABBR_EVADE            = "EVA";
    String ABBR_HIT_RATE         = "HRt";
    String ABBR_HITTING          = "HIT";
    String ABBR_IQ               = "IQ";
    String DESC_ACCURACY         = "Accuracy";
    String ABBR_RESISTANCE       = "RES";
    
    //
    
    String DESC_ATTACK_SKILL     = "Attack Skill";
    String DESC_ATTACK_SPEED     = "Attack Speed";
    String DESC_CAST_RATE        = "Cast rate";
    String DESC_COMBAT_QUICKNESS = "Combat Quickness";
    String DESC_COMBAT_RATING    = "Combat rating";
    String DESC_CRITICAL_CHANCE  = "Critical Chance";
    String DESC_DEFENCE_RATE     = "Defence Rate";
    String DESC_ENERGY           = "Energy";
    String DESC_EVADE            = "Evade";
    String DESC_HIT_RATE         = "Hit Rate";
    String DESC_HITTING          = "Hitting";
    String DESC_INTELLIGENCE     = "The ability to learn new thing and solve problems.";
    String DESC_IQ               = "IQ";
    String DESC_KNOWLEDGE        = "Knowledge";
    String DESC_LIFE             = "Hit Points";
    String DESC_MANA             = "Mana Points";
    String DESC_PSIONIC          = "Increace effectiveness of certain magical and mental abilities";
    String DESC_RESISTANCE       = "Resistance";
    String DESC_SPIRIT           = "Ability to recover from magical, mental and spiritual affects";
    String DESC_VIGOUR           = "Vigour";
    
    String NAME_ACCURACY         = "Accuracy";
    String NAME_ARMOR_PIERCING   = "Armor Piercing";
    String NAME_ATTACK_SKILL     = "Attack Skill";
    String NAME_ATTACK_SPEED     = "Attack Speed";
    String NAME_CAST_RATE        = "Cast rate";
    String NAME_CHAR             = "Charisma";
    String NAME_COMBAT_QUICKNESS = "Combat Quickness";
    String NAME_COMBAT_RATING    = "Combat rating";
    String NAME_CRITICAL_CHANCE  = "Critical Chance";
    String NAME_DEFENCE_RATE     = "Defence Rate";
    String NAME_ENERGY           = "Energy";
    String NAME_EVADE            = "Evade";
    String NAME_HIT_RATE         = "Hit Rate";
    String NAME_HITTING          = "Hitting";
    String NAME_INTELLIGENCE     = "Intelligence";
    String NAME_IQ               = "IQ";
    String NAME_KNOWLEDGE        = "Knowledge";
    String NAME_LIFE             = "Hit Points";
    String NAME_MANA             = "Mana Points";
    String NAME_PSIONIC          = "Psionic";
    String NAME_RESISTANCE       = "Resistance";
    String NAME_SPIRIT           = "Spirit";
    String NAME_VIGOUR           = "Vigour";
    
    // Attribute Group
    String NAME_PHYSICAL  = "Physique";
    String NAME_PSYCHICAL = "Psyche";
    String NAME_PERSON    = "Personality";
    
    String ABBR_PHYSICAL  = "PH";
    String ABBR_PSYCHICAL = "PS";
    String ABBR_PERSON    = "PN";
    
    String DESC_PHYSICAL  = "";
    String DESC_PSYCHICAL = "Ability to continue works under stress.";
    String DESC_PERSON    = "Sliders that hold information about person";
}
