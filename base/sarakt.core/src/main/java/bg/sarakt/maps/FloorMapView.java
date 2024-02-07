/*
 * MapDisplay.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps;

import bg.sarakt.base.IPosition;

public interface FloorMapView<T extends IPosition> extends Iterable<TileView>
{
    TileView getTile(T position);
}
