/*
 * TileViewImpl.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps.impls;

import bg.sarakt.maps.TileView;

public record TileViewImpl(Object printView, boolean lastColumn) implements TileView
{

}
