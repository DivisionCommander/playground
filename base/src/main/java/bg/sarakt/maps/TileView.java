/*
 * TileView.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps;


import bg.sarakt.maps.impls.TileViewImpl;


public interface TileView extends ITile
{
    public static final TileView UNKNOWN = new TileViewImpl("~", false);

    Object printView();


    boolean lastColumn();
}
