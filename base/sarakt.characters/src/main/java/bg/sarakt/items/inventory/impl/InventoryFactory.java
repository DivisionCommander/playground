/*
 * InventoryFactory.java
 *
 * created at 2024-01-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.items.inventory.impl;

import java.util.HashMap;
import java.util.Map;

import bg.sarakt.items.inventory.Inventory;
import bg.sarakt.items.inventory.equipment.EquipmentManager;
import bg.sarakt.items.inventory.equipment.EquipmentSlots;
import bg.sarakt.items.inventory.equipment.impl.EquipmentManagerImpl;

public final class InventoryFactory {

    public static Inventory defaultInventory() {
        Map<EquipmentSlots, Integer> def = new HashMap<>();
        def.put(EquipmentSlots.MAIN_HAND, 1);
        def.put(EquipmentSlots.OFF_HAND, 1);
        def.put(EquipmentSlots.EXTRA, 1);
        def.put(EquipmentSlots.HEAD, 1);
        def.put(EquipmentSlots.TORSO, 1);
        def.put(EquipmentSlots.WAIST, 1);
        def.put(EquipmentSlots.LEG, 1);
        def.put(EquipmentSlots.FEET, 1);
        def.put(EquipmentSlots.WRISTS, 1);
        def.put(EquipmentSlots.HANDS, 1);
        def.put(EquipmentSlots.RING, 4);
        def.put(EquipmentSlots.NECK, 1);
        def.put(EquipmentSlots.TRINKET, 2);

        return createInventory(def);
    }

    public static Inventory createInventory(long mask) {
        Map<EquipmentSlots, Integer> slotMap = EquipmentSlots.calculateSlotMap(mask);
        return createInventory(slotMap);
    }

    public static Inventory createInventory(Map<EquipmentSlots, Integer> slotMap) {
        EquipmentManager em = new EquipmentManagerImpl(slotMap);

        return new InventoryImpl(em);
    }
}
