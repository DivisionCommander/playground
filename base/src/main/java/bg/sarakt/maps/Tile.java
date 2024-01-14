/*
 * Tile.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps;


public interface Tile extends ITile
{
    TileType getType();


    boolean isPassable();

    // Consider automatically convert tile to view. Requires dropping of lastColumn field
    // TileView toView()
}
