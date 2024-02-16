/*
 * Equipment.java
 *
 * created at 2024-01-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.items.inventory.equipment;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.base.utils.ForRemoval;
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
    
    /**
     * @deprecated Use {@link AttributeModifier} instead.
     */
    @Deprecated(since = "0.0.3", forRemoval = true)
    @ForRemoval(since = "0.0.3", expectedRemovalVersion = "0.0.5")
    Set<AttributeValuePair> getBonuses();
    
    /**
     * Replace now deprecated {@link Equipment#getModifiers()}. Default
     * implementation will be dropped in version=0.0.5.
     * 
     * @since 0.0.3
     */
    default List<AttributeModifier<Attribute>> getModifiers() { return Collections.emptyList(); }
    
    Map<EquipmentSlots, Integer> getLockedSlots();

    @Override
    EquipmentView getView();
}
