/*
 * Position2D.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.base.utils;


import bg.sarakt.base.Position2D;


public final class Positions2D
{
    private static final Position2D DEFAULT_POSITION = new Position2D(0, 0);

    public static final Position2D getDefaultPosition()
    {
        return DEFAULT_POSITION;
    }


    public static Position2D nextColumn(Position2D current)
    {
        return new Position2D(current.latitude(), current.longitude() + 1);
    }


    public static Position2D nextRow(Position2D currentPosition)
    {
        return new Position2D(currentPosition.latitude() + 1, currentPosition.longitude());
    }


    public static Position2D nextRowAtStart(Position2D current)
    {
        return new Position2D(current.latitude() + 1, 0);
    }


    public static Position2D goNorth(Position2D current)
    {
        return goNorth(current, 1);
    }


    public static Position2D goNorth(Position2D current, int steps)
    {
        return new Position2D((current.latitude() - steps), current.latitude());
    }


    public static Position2D goSouth(Position2D current)
    {
        return goSouth(current, 1);
    }


    public static Position2D goSouth(Position2D current, int steps)
    {
        return new Position2D((current.latitude() + steps), current.latitude());
    }


    public static Position2D goEast(Position2D current)
    {
        return goEast(current, 1);
    }


    public static Position2D goEast(Position2D current, int steps)
    {
        return new Position2D(current.latitude(), (current.longitude() + steps));
    }

}
