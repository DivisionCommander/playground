/*
 * Checkers.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.base.utils;

import bg.sarakt.base.Position2D;

public final class Checkers
{

    public static final String INVALID_POSITION = "Invalid position or coordinates.".intern();

    public static void checkPosition(Position2D position)
    {
        if (position == null)
        {
            throw new IllegalAccessError(INVALID_POSITION);
        }
        // For now only positive positions are allowed:
        if (position.latitude() < 0 || position.longitude() < 0)
        {
            throw new IllegalArgumentException(INVALID_POSITION);
        }
    }

    public static void checkPositionLongitude(Position2D position, int longitude)
    {
        checkPosition(position);
        checkCoordinates(position.longitude(), longitude);
    }

    public static void checkPositionLatitude(Position2D position, int latitude)
    {
        checkPosition(position);
        checkCoordinates(position.latitude(), latitude);

    }

    private static void checkCoordinates(int actual, int expected)
    {
        if (expected >= actual)
        {
            throw new IllegalArgumentException(INVALID_POSITION);
        }

    }
}
