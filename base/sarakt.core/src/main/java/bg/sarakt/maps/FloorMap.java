/*
 * FloorMap.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps;


import bg.sarakt.base.IPosition;
import bg.sarakt.characters.GameCharacter;


public interface FloorMap<T extends IPosition>
{
    MapTile getMapTile(T position);


    boolean isOccupied(T position);


    void occupy(T position, GameCharacter character);


    void moveCharacter(GameCharacter character, T from, T to);


    FloorMapView<T> getMapView();


    GameCharacter getOccupant(T position);

}
