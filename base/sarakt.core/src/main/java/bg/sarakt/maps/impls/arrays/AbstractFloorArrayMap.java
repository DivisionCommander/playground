/*
 * AbstractFloorArrayMap.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps.impls.arrays;


import java.util.Iterator;
import java.util.NoSuchElementException;

import bg.sarakt.base.AbstractGameObject;
import bg.sarakt.base.GameObject;
import bg.sarakt.base.Position2D;
import bg.sarakt.base.utils.Checkers;
import bg.sarakt.base.utils.Positions2D;
import bg.sarakt.maps.ITile;


abstract class AbstractFloorArrayMap<T extends ITile> extends AbstractGameObject implements GameObject, Iterable<T>
{

    protected static final String representationSignatureID = "2023-nov-23-00:47-ABSTRACTION";
    private final long instanceId;
    protected T[][] map;

    protected AbstractFloorArrayMap(T[][] map)
    {
        this(map, System.nanoTime());
    }


    /**
     * @param map
     * @param nanoTime
     */
    protected AbstractFloorArrayMap(T[][] map, long id)
    {
        this.map = map;
        this.instanceId = id;
    }


    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator()
    {
        return new IteratorImpl();
    }


    protected T getTile(Position2D position)
    {
        checkPosition(position);
        return map[position.longitude()][position.latitude()];

    }


    protected void checkPosition(Position2D position)
    {
        Checkers.checkPosition(position);
        Checkers.checkPositionLongitude(position, map.length);
        Checkers.checkPositionLatitude(position, map[position.longitude()].length);
    }


    /**
     * @see bg.sarakt.base.GameObject#id()
     */
    @Override
    public String id()
    {
        return representationSignatureID;
    }


    /**
     * @see bg.sarakt.base.GameObject#instanceId()
     */
    @Override
    public long instanceId()
    {
        return instanceId;
    }

    protected class IteratorImpl implements Iterator<T>
    {

        private Position2D nextPosition = Positions2D.getDefaultPosition();

        /**
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext()
        {
            return hasValidNextPosition(nextPosition);
        }


        /**
         * @see java.util.Iterator#next()
         */
        @Override
        public T next()
        {
            if (hasNext())
            {
                T tile = map[nextPosition.latitude()][nextPosition.longitude()];
                nextPosition = nextPosition();
                return tile;
            }
            throw new NoSuchElementException();
        }


        private Position2D nextPosition()
        {
            Position2D pos = nextPosition;
            if (pos.latitude() >= map.length)
            {
                return null;
            }
            pos = Positions2D.nextColumn(pos);

            if (pos.longitude() >= map[pos.latitude()].length)
            {
                pos = Positions2D.nextRowAtStart(pos);
                if (pos.latitude() >= map.length)
                {
                    return null;
                }
                return pos;
            }
            return pos;

        }


        private boolean hasValidNextPosition(Position2D pos)
        {
            if (pos == null)
            {
                return false;
            }
            return pos.latitude() < map.length;
        }

    }

}
