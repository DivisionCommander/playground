/*
 * GameCharacter.java
 *
 * created at 2023-11-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.characters.attributes1.AttributeMap;
import bg.sarakt.characters.attributes1.UnitClass;

public interface GameCharacter extends GameUnit {

    UnitClass characterClass();

    long currentExperience();

    void gainExperience(long experience);

    AttributeValuePair getAttribute(Attribute attr);

    @Deprecated
    AttributeMap getAttributeMap();

    Biography getBiography();

    void levelUp(Level level);
}
