/*
 * FloorMapViewImpl.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps.impls.arrays;


import bg.sarakt.base.Position2D;
import bg.sarakt.maps.FloorMapView;
import bg.sarakt.maps.TileView;


/**
 * TODO short description for FloorMapViewImpl.
 * <p>
 * Long description for FloorMapViewImpl.
 *
 * @author IceDragon
 */
public class FloorMapViewArrayImpl extends AbstractFloorArrayMap<TileView> implements FloorMapView<Position2D>
{
    protected static final String representationSignatureID = "2023-nov-23-00:52-ARRAY-MAP-VIEW";

    public FloorMapViewArrayImpl(TileView[][] view)
    {
        this(view, System.currentTimeMillis());
    }


    public FloorMapViewArrayImpl(TileView[][] view, long id)
    {
        super(view);
    }


    /**
     * @see bg.sarakt.maps.impls.arrays.AbstractFloorArrayMap#getTile(bg.sarakt.base.Position)
     */
    @Override
    public TileView getTile(Position2D position)
    {
        return super.getTile(position);
    }


    /**
     * @see bg.sarakt.maps.impls.arrays.AbstractFloorArrayMap#id()
     */
    @Override
    public String id()
    {
        return representationSignatureID;
    }

}
