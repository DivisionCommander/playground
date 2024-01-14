/*
 * InventorySlot.java
 *
 * created at 2019-03-20 by r.tsonev <YOURMAILADDRESS>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */

package bg.sarakt.items.inventory.equipment;

import java.util.EnumMap;
import java.util.Map;

public enum EquipmentSlots
{

    // Backpack, handle differently
    BACKPACK("Backpack", 100_000_000_000_000_000L),

    // Weapon slots
    MAIN_HAND("Weapon", 1L),
    OFF_HAND("Shield", 10L),
    EXTRA("Extra", 100L),

    // Armour slots
    HEAD("Head", 1_000L),
    TORSO("Torso", 10_000L),
    WAIST("Waist", 100_000L),
    LEG("Legs", 1_000_000L),
    FEET("Feet", 10_000_000L),
    WRISTS("Wrists", 100_000_000L),
    HANDS("Hands", 1_000_000_000L),

    // Jewelry slots
    RING("Finger", 10_000_000_000L),
    NECK("Neck", 100_000_000_000L),
    TRINKET("Trinket", 1_000_000_000_000L),
    OTHER("Other", 10_000_000_000_000L),

    /*
    @formatter:off
    // Unused slots, mask reservation
    // 000_000_000_000_000_000
    UNUSED1("Unused", 000_100_000_000_000_000L),
    UNUSED2("Unused", 001_000_000_000_000_000L),
    UNUSED3("Unused", 010_000_000_000_000_000L),

    @formatter:on
     */
    ;

    private final String name;
    private final long   mask;

    private EquipmentSlots(String name, long mask) {
        this.name = name;
        this.mask = mask;
    }

    public long getMask() { return this.mask; }

    public String getName() { return name; }

    public static Map<EquipmentSlots, Integer> calculateSlotMap(long mask) {
        long result = mask;
        EnumMap<EquipmentSlots, Integer> amounts = new EnumMap<>(EquipmentSlots.class);
        for (int i = 0; i < values().length; i++) {
            EquipmentSlots slot = values()[i];
            Long amount = result % 10;
            amounts.put(slot, amount.intValue());
            result /= 10;
            System.out.println(slot.name + " \t= " + amount + "\t left=" + result);
        }
        return amounts;
    }

    /**
     * Calculate a valid mask based on available slots per {@link EquipmentSlots}
     *
     * @param map
     * @return
     */
    public static long calculateMask(Map<EquipmentSlots, Long> map) {
        long mask = 0;
        for (EquipmentSlots s : map.keySet()) {
            Long amount = map.get(s);
            if (amount == null) {
                amount = 0L;
            }
            mask += (s.mask * amount);
        }
        return mask;
    }
}
