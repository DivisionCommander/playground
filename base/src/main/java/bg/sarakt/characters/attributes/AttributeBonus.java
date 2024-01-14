/*
 * AttributeBonus.java
 *
 * created at 2024-01-12 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes;

import bg.sarakt.attributes.Attribute;

public interface AttributeBonus {

    Attribute getAttribute();

    <T extends Number> T getValue();

    BonusType getBonusType();

    ApplyLevel getApplyLevel();

    static enum BonusType
    {
        FLAT,
        COEFFICIENT
    }

    static enum ApplyLevel
    {
        BASELINE,
        CLASS_LEVEL,
        GEAR_LEVEL,
        SPELL_LEVEL,
    }
}
