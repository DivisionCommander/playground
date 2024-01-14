/*
 * MapManager.java
 *
 * created at 2023-11-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps;


import bg.sarakt.base.IPosition;
import bg.sarakt.characters.GameCharacter;


public class MapManager<T extends IPosition>
{
    protected FloorMap<T> map;

    public MapManager(FloorMap<T> map)
    {
        this.map = map;
    }


    public void moveCharacter(GameCharacter character, T from, T to)
    {
        map.moveCharacter(character, from, to);
    }


    public void spawnCharacter(GameCharacter character, T position)
    {
        this.map.occupy(position, character);
    }


    public void combat(GameCharacter characer, T position)
    {
        // GameCharacter occupant =
        this.map.getOccupant(position);

        // Combat logic comes here
        // new Combat(character, occupant);
    }


    public FloorMapView<T> getMapView()
    {
        return this.map.getMapView();
    }

}
