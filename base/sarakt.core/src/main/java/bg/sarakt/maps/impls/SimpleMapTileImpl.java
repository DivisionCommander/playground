/*
 * MapTileImpl.java
 *
 * created at 2023-11-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps.impls;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import bg.sarakt.characters.GameCharacter;
import bg.sarakt.items.basics.Item;
import bg.sarakt.maps.MapTile;


public class SimpleMapTileImpl implements MapTile
{

    private GameCharacter occupant = null;
    private Collection<Item> loot;
    private boolean isPassable;

    public SimpleMapTileImpl(boolean passable)
    {
        this.isPassable = passable;
        this.loot = new ArrayList<>();
    }


    public SimpleMapTileImpl(boolean passable, Item loot)
    {
        this(passable);
        this.loot.add(loot);
    }


    public SimpleMapTileImpl(boolean passable, Collection<Item> loot)
    {
        this(passable);
        if (loot != null && !loot.isEmpty())
        {
            this.loot.addAll(loot);
        }
    }


    /**
     * @see bg.sarakt.maps.MapTile#hasOccupants()
     */
    @Override
    public boolean hasOccupants()
    {
        return occupant != null;
    }


    /**
     * @see bg.sarakt.maps.MapTile#occupy(GameCharacter)
     */
    @Override
    public void occupy(GameCharacter occupant)
    {
        if (hasOccupants() && occupant.isAlive())
        {
            throw new UnsupportedOperationException("Tile is already occupied");
        }
        this.occupant = occupant;
    }


    public void addLoot(Item newLoot)
    {
        this.loot.add(newLoot);
    }


    protected void addLoot(Collection<Item> newLoot)
    {
        this.loot.addAll(newLoot);
    }


    /**
     * @see bg.sarakt.maps.MapTile#isPassable()
     */
    @Override
    public boolean isPassable()
    {
        return isPassable && !hasOccupants();
    }


    /**
     * @see bg.sarakt.maps.MapTile#getActions()
     */
    @Override
    public Collection<Object> getActions()
    {
        return Collections.emptySet();
    }


    /**
     * @see bg.sarakt.maps.MapTile#getLoot()
     */
    @Override
    public Collection<Item> getLoot()
    {
        Collection<Item> drop = new ArrayList<>(this.loot);
        this.loot.clear();
        return drop;
    }


    /**
     * @see bg.sarakt.maps.MapTile#getOccupant()
     */
    @Override
    public GameCharacter getOccupant()
    {
        return occupant;
    }


    /**
     * @see bg.sarakt.maps.MapTile#hasLoot()
     */
    @Override
    public boolean hasLoot()
    {
        return !loot.isEmpty();
    }


    /**
     * @see bg.sarakt.maps.MapTile#hasAction()
     */
    @Override
    public boolean hasAction()
    {
        return false;
    }

}
