/*
 * ItemType.java
 *
 * created at 2024-01-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.items.basics;

public enum ItemType
{

    WEAPON("Weapon"),
    ARMOUR("Armour"),
    AMMO("Ammunition"),
    JUNK("Junk"),
    QUEST("Quest Item"),
    CONSUMABLE("Consumable"),
    REAGENT("Reagent"),
    KEY("Key"),

    ;

    private final String name;

    private ItemType(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }
}
