/*
 * EquipmentManager.java
 *
 * created at 2024-01-10 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.items.inventory.equipment;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import bg.sarakt.base.exceptions.InventoryException;
import bg.sarakt.characters.attributes.AttributeValuePair;

public interface EquipmentManager {

    /**
     * Check if presented {@link Equipment} can be equipped by the current
     * {@link EquipmentManager}.
     *
     * @param equipment
     *            to be checked
     * @return <b>true</b> if the equipment can be equipped<br>
     *         <b>false</b> elsewhere.
     */
    boolean canEquip(Equipment equipment);

    /**
     * Attempt to equip the Equipment into first available position.
     *
     * @param equipment
     *            to be equipped
     * @return <b>{@link Optional#empty()}</b> if the equip is successful.<br>
     *         provided equipment if not.
     * @throws InventoryException
     */
    Optional<Equipment> equip(Equipment equipment) throws InventoryException;

    /**
     * Attempt to equip the {@link Equipment} into specific position in the slot,
     * e.g. a ring into the second {@link EquipmentSlots#RING} position.
     *
     * @param equipment
     * @param position
     * @return the previously equipped {@link Equipment} in the slot or<br>
     *         {@link Optional#empty()} if there is nothing there.
     * @throws InventoryException
     */
    Optional<Equipment> equip(Equipment equipment, int position) throws InventoryException;

    /**
     * Swap equipment into two different position in one {@link EquipmentSlots},
     * e.g. two rings into two separate positions in the
     * {@link EquipmentSlots#RING}.
     *
     * @param equipmentSlot
     * @param firstPosition
     * @param secondPosition
     * @throws InventoryException
     */
    void swap(EquipmentSlots equipmentSlot, int firstPosition, int secondPosition) throws InventoryException;

    /**
     * Attempt to remove any previously equipped item in the slot and position.
     *
     * @param slot
     * @param position
     * @return the previously equipped {@link Equipment} in the slot or<br>
     *         {@link Optional#empty()} if there is nothing there.
     * @throws InventoryException
     */
    Optional<Equipment> unequip(EquipmentSlots slot, int position) throws InventoryException;

    /**
     * Attempt to unequip all equipments in the {@link EquipmentSlots}.
     *
     * @param equipmentSlot
     * @return
     * @throws InventoryException
     */
    List<Optional<Equipment>> unequipSlot(EquipmentSlots equipmentSlot) throws InventoryException;

    /**
     * Attempt to remove all equipped {@link Equipment} from any
     * {@link EquipmentSlots}.
     *
     * @return
     * @throws InventoryException
     */
    List<Optional<Equipment>> unequipAll() throws InventoryException;

    List<EquipmentView> getEquipmentViews();

    Map<EquipmentSlots, List<EquipmentView>> getEquipmentViewMap();

    Collection<AttributeValuePair> getBonusAttributes();

    /**
     * Check if there is at least one free position for provided
     * {@link EquipmentSlots}
     *
     * @param equipmentSlot
     * @return <b>true</b> if there is a position<br>
     *         <b>false</b> if currently there are no free positions.
     */
    boolean hasFreePosition(EquipmentSlots equipmentSlot);

    /**
     * Get the first free position for the provided {@link EquipmentSlots}.
     *
     * @param equipmentSlot
     * @return
     */
    int getFirstFreePosition(EquipmentSlots equipmentSlot);
}
