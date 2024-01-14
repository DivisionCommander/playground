/*
 * Equipment.java
 *
 * created at 2024-01-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.items.inventory.equipment;

import java.util.Map;
import java.util.Set;

import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.items.basics.Item;

public interface Equipment extends Item {

    /**
     * By default any piece of equipment is equippable. Any non-equippable cases
     * must be handled manually.
     *
     * @see bg.sarakt.items.basics.Item#isEquippable()
     */
    @Override
    default boolean isEquippable() { return true; }

    EquipmentSlots getSlot();

    Set<AttributeValuePair> getBonuses();

    Map<EquipmentSlots, Integer> getLockedSlots();

    @Override
    EquipmentView getView();
}
