/*
 * Species.java
 *
 * created at 2024-01-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.glossary;

public class Species {

    private final String name;
    private long         equipmentMask;

    public Species(String name) {
        this.name = name;
    }


    public String getName() { return name; }


    public long getEquipmentMask() { return equipmentMask; }


    public void setEquipmentMask(long equipmentMask) { this.equipmentMask = equipmentMask; }
}
