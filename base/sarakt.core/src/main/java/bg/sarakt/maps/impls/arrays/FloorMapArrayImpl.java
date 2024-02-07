/*
 * FloorMapImpl.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.maps.impls.arrays;

import java.util.HashMap;
import java.util.Map;

import bg.sarakt.base.IPosition;
import bg.sarakt.base.Position2D;
import bg.sarakt.base.exceptions.SaraktRuntimeException;
import bg.sarakt.characters.GameCharacter;
import bg.sarakt.items.basics.Item;
import bg.sarakt.maps.FloorMap;
import bg.sarakt.maps.FloorMapView;
import bg.sarakt.maps.MapTile;
import bg.sarakt.maps.Tile;
import bg.sarakt.maps.TileView;
import bg.sarakt.maps.impls.SimpleMapTileImpl;
import bg.sarakt.maps.impls.TileViewImpl;

public class FloorMapArrayImpl
    extends
    AbstractFloorArrayMap<Tile> implements FloorMap<Position2D>
{

    protected static final String         representationSignatureID = "2023-nov-23-01:03-ARRAY-MAP";
    private Map<IPosition, GameCharacter> occupants;
    private Map<IPosition, Item>          loots;

    public FloorMapArrayImpl(Tile[][] tiles)
    {
        this(tiles, System.currentTimeMillis());
    }

    public FloorMapArrayImpl(Tile[][] tiles, long id)
    {
        super(tiles, id);
        this.occupants = new HashMap<>();
        this.loots = new HashMap<>();
    }

    /**
     * @see bg.sarakt.maps.FloorMap#getMapTile(bg.sarakt.base.IPosition)
     */
    @Override
    public MapTile getMapTile(Position2D position)
    {
        Tile tile = getTile(position);
        SimpleMapTileImpl smti = new SimpleMapTileImpl(tile.isPassable());
        if (occupants.containsKey(position))
        {
            smti.occupy(occupants.get(position));
        }
        Item loot = loots.get(position);
        if (loot != null)
        {
            smti.addLoot(loot);
        }
        return smti;
    }

    /**
     * @see bg.sarakt.maps.FloorMap#isOccupied(bg.sarakt.base.IPosition)
     */
    @Override
    public boolean isOccupied(Position2D position)
    {
        return occupants.containsKey(position);
    }

    /**
     * @see bg.sarakt.maps.FloorMap#getOccupant(bg.sarakt.base.IPosition)
     */
    @Override
    public GameCharacter getOccupant(Position2D position)
    {
        return occupants.get(position);
    }

    private boolean checkOccupant(GameCharacter characater, Position2D position)
    {
        GameCharacter occupant = occupants.get(position);
        return characater.compareTo(occupant) == 0;
    }

    /**
     * @see bg.sarakt.maps.FloorMap#occupy(bg.sarakt.base.IPosition,
     *      bg.sarakt.characters.GameCharacter)
     */
    @Override
    public void occupy(Position2D position, GameCharacter character)
    {
        checkPosition(position);
        occupants.put(position, character);
    }

    @Override
    public void moveCharacter(GameCharacter character, Position2D from, Position2D to)
    {
        if (from.equals(to) || occupants.containsKey(to) || !checkOccupant(character, from))
        {
            throw new SaraktRuntimeException("Cannot move character " + character.name() + "from: " + from + " to " + to);
        }
        occupants.remove(from);
        occupants.put(to, character);
    }

    /**
     * @see bg.sarakt.maps.FloorMap#getMapView()
     */
    @Override
    public FloorMapView<Position2D> getMapView()
    {

        TileView[][] view = new TileView[map.length][];
        for (int latitude = 0; latitude < map.length; latitude++)
        {
            Tile[] row = map[latitude];
            TileView[] viewRow = new TileView[row.length];
            for (int longitude = 0; longitude < row.length; longitude++)
            {
                Tile tile = row[longitude];
                // Consider Tile#toView();
                boolean lastColumn = (row.length - 1) == longitude;
                TileView tileView = tile == null ? TileView.UNKNOWN : new TileViewImpl(tile.getType().toString(), lastColumn);

                viewRow[longitude] = tileView;

            }
            view[latitude] = viewRow;
        }

        return new FloorMapViewArrayImpl(view);
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
