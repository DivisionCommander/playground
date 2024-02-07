/*
 * TileTypes.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps.impls;

import bg.sarakt.maps.TileType;

public enum BasicTileTypes implements TileType
{
    WALL('⛓', false),
    GLASS('☷', false),
    CORRIDOR('-'),
    DOOR('◊'),
    LOCKED('⚿', false),
    BLOCKED('⚷', false),
    LIFT('⛲'),
    ROCK('◙', false),
    ROOM(' '),
    UNKNOWN('‽'),
    TRAP('Ѧ'),
    PANEL('⌨'),
    ;

    private final char symbol;
    private final boolean passable;

    private BasicTileTypes(char icon)
    {
        this(icon, true);
    }

    private BasicTileTypes(char icon, boolean passable)
    {
        this.symbol = icon;
        this.passable = passable;
    }

    public char getChar()
    {
        return symbol;
    }


    public String getString()
    {
        return String.valueOf(symbol);
    }
    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
        return String.valueOf(symbol);
    }

    public boolean isPassable()
    {
        return passable;
    }
}



