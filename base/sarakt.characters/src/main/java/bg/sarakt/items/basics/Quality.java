/*
 * Quality.java
 *
 * created at 2024-01-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.items.basics;

public enum Quality
{

    UNKNOWN("Unknown", Double.NaN),

    LEGENDARY("Legendary",  1.5d),
    EXTRA("Extraordinary", 1.2d),
    NORMAL("Regular", 1.0d),
    RENEWED("Renewed", 0.999d),
    DAMAGED("Damaged", 0.6d),
    RUINED("Ruined", 0.4d),

    ;

    private final String name;
    private final double modifier;

    private Quality(String name, double modifier) {
        this.name = name;
        this.modifier = modifier;
    }

    public String getName() { return this.name; }

    public double getModifier() { return this.modifier; }
}
