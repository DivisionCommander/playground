/*
 * MapPosition.java
 *
 * created at 2023-11-19 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.base;


/**
 * latitude is parallel (Row)
 * longitude is meridian (column)
 *
 * @author IceDragon
 */

public record Position2D(int latitude, int longitude) implements IPosition
{
}
