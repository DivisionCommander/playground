/*
 * LevelUp.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

import bg.sarakt.attributes.impl.PrimaryAttribute;

public interface Level {

    Map<PrimaryAttribute, BigDecimal> getPrimaryAttributeBonuses();

    Integer getLevelNumber();

    Level getNextLevel();

    public static final Level TEMP = new Level()
    {

        @Override
        public Map<PrimaryAttribute, BigDecimal> getPrimaryAttributeBonuses() { // TODO Auto-generated method stub
            EnumMap<PrimaryAttribute, BigDecimal> map = new EnumMap<>(PrimaryAttribute.class);
            PrimaryAttribute.getAllPrimaryAttributes().stream().forEach(pa -> map.put(pa, BigDecimal.valueOf(3)));
            return map;
        }

        @Override
        public Level getNextLevel() { // TODO Auto-generated method stub
            return this;
        }

        @Override
        public Integer getLevelNumber() { // TODO Auto-generated method stub
            return -1;
        }
    };

}