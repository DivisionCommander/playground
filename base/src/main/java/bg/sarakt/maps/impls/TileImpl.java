/*
 * TileImpl.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps.impls;

import bg.sarakt.maps.Tile;
import bg.sarakt.maps.TileType;

public class TileImpl implements Tile
{
    private final TileType type;
    private final boolean isPassible;

    public TileImpl(BasicTileTypes type)
    {
        this(type, true);
    }


    public TileImpl(TileType type, boolean isPassible)
    {
        this.type = type;
        this.isPassible = isPassible;
    }


    /**
     * @see bg.sarakt.maps.Tile#getType()
     */
    @Override
    public TileType getType()
    {
        return type;
    }


    /**
     * @see bg.sarakt.maps.Tile#isPassable()
     */
    @Override
    public boolean isPassable()
    {
        return isPassible;
    }

}
