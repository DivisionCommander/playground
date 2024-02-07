/*
 * Level.java
 *
 * created at 2024-01-31 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.attributes.levels.impl.DummyLevelImpl;

public interface Level {

    Map<PrimaryAttribute, BigInteger> getPermanentBonuses();

    default List<AttributeModifier<Attribute>> getAllModifiers(){return Collections.emptyList();}

    Integer getLevelNumber();

    int getUnallocatedPonts();

    boolean earnExperience(BigInteger amount);

    BigInteger currentExperience();
    
    LevelNode viewPreviousLevel();

    LevelNode viewCurrentLevel() ;

    LevelNode viewNextLevel();

    public static final Level TEMP = new DummyLevelImpl();

}