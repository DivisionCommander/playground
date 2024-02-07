/*
 * MapTile.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps;


import java.util.Collection;

import bg.sarakt.characters.GameCharacter;
import bg.sarakt.items.basics.Item;


public interface MapTile
{
    boolean hasOccupants();


    GameCharacter getOccupant();


    boolean hasLoot();


    Collection<Item> getLoot();


    boolean hasAction();


    Collection<Object> getActions();


    boolean isPassable();


    void occupy(GameCharacter occupant);
}
