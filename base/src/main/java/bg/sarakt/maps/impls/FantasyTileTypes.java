/*
 * FantasyTileTypes.java
 *
 * created at 2023-11-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps.impls;


import java.util.EnumSet;
import java.util.Set;

import bg.sarakt.maps.TileType;


/**
 * TODO short description for FantasyTileTypes.
 * <p>
 * Long description for FantasyTileTypes.
 *
 * @author IceDragon
 */
public enum FantasyTileTypes implements TileType
{
    PINES('^'),
    OAK('&'),
    RIVER('~'),
    ROAD('-'),
    GRAVE('+'),
    WALL('=')

    ;

    private static final Set<FantasyTileTypes> ALL_TYPES = EnumSet.allOf(FantasyTileTypes.class);

    private final char symbol;

    private FantasyTileTypes(char ch)
    {
        this.symbol = ch;
    }


    public static TileType getType(char symbol)
    {
        for (FantasyTileTypes type : ALL_TYPES)
        {
            if (type.symbol == symbol)
            {
                return type;
            }
        }
        return null;
    }


    public static final Set<FantasyTileTypes> getAllFantasyTypes()
    {
        return ALL_TYPES;
    }


    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
        return String.valueOf(symbol);
    }
}
