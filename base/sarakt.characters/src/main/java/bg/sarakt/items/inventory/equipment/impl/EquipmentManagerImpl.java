/*
 * EquipmentManagerImpl.java
 *
 * created at 2024-01-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.items.inventory.equipment.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import bg.sarakt.base.exceptions.InventoryException;
import bg.sarakt.items.inventory.equipment.Equipment;
import bg.sarakt.items.inventory.equipment.EquipmentManager;
import bg.sarakt.items.inventory.equipment.EquipmentSlots;
import bg.sarakt.items.inventory.equipment.EquipmentView;

public class EquipmentManagerImpl extends AbstractEquipmentManager implements EquipmentManager {

    // TODO: migrate to factory model with selectable type of manager.
    public EquipmentManagerImpl(Map<EquipmentSlots, Integer> slots) {
        super(slots);
    }

    /**
     *
     * @see bg.sarakt.items.inventory.equipment.EquipmentManager#equip(bg.sarakt.items.inventory.equipment.Equipment)
     */
    @Override
    public Optional<Equipment> equip(Equipment equipment) throws InventoryException {
        SlotPositions slot = getSlot(equipment.getSlot());
        if ( !canEquip(equipment)) {
            throw new InventoryException("Cannot equip <" + equipment + ">");
        }
        if ( !slot.hasFreePosition()) {
            int position = slot.getFirstfreePosition();
            slot.equip(equipment, position);
            lockPositions(equipment);
            applyBonuses(equipment);
            return Optional.empty();
        }
        return Optional.of(equipment);
    }

    /**
     *
     * @see bg.sarakt.items.inventory.equipment.EquipmentManager#equip(bg.sarakt.items.inventory.equipment.Equipment,
     *      int)
     */
    @Override
    public Optional<Equipment> equip(Equipment equipment, int position) throws InventoryException {
        SlotPositions slot = getSlot(equipment.getSlot());
        slot.checkPosition(position);
        Optional<Equipment> unequipped = unequip(slot, position);
        equip(slot, position, equipment);
        return unequipped;
    }

    /**
     *
     * @see bg.sarakt.items.inventory.equipment.EquipmentManager#swap(bg.sarakt.items.inventory.equipment.EquipmentSlots,
     *      int, int)
     */
    @Override
    public void swap(EquipmentSlots equipmentSlot, int firstPosition, int secondPosition) throws InventoryException {
        SlotPositions slot = getSlot(equipmentSlot);
        slot.checkPosition(firstPosition);
        slot.checkPosition(secondPosition);
        slot.swap(firstPosition, secondPosition);
    }

    /**
     *
     * @see bg.sarakt.items.inventory.equipment.EquipmentManager#unequip(bg.sarakt.items.inventory.equipment.EquipmentSlots,
     *      int)
     */
    @Override
    public Optional<Equipment> unequip(EquipmentSlots slot, int position) throws InventoryException {
        return unequip(getSlot(slot), position);
    }

    /**
     *
     * @see bg.sarakt.items.inventory.equipment.EquipmentManager#unequipSlot(bg.sarakt.items.inventory.equipment.EquipmentSlots)
     */
    @Override
    public List<Optional<Equipment>> unequipSlot(EquipmentSlots equipmentSlot) {
        List<Optional<Equipment>> equipment = getSlot(equipmentSlot).removeAll();
        equipment.forEach(this::removeBonuses);
        return equipment;
    }

    /**
     *
     * @see bg.sarakt.items.inventory.equipment.EquipmentManager#unequipAll()
     */
    @Override
    public List<Optional<Equipment>> unequipAll() {
        return equipped.values().stream().map(SlotPositions::removeAll).flatMap(Collection::stream).filter(Optional::isEmpty).map(this::removeBonuses)
                .toList();
    }

    @Override
    public List<EquipmentView> getEquipmentViews() {
        return equipped.values().stream().map(SlotPositions::convertToView).flatMap(Collection::stream).toList();
    }

    /**
     *
     * @see bg.sarakt.items.inventory.equipment.EquipmentManager#getEquipmentViewMap()
     */
    @Override
    public Map<EquipmentSlots, List<EquipmentView>> getEquipmentViewMap() {
        return equipped.entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> e.getValue().convertToView()));
    }

    /**
     *
     * @see bg.sarakt.items.inventory.equipment.EquipmentManager#hasFreePosition(bg.sarakt.items.inventory.equipment.EquipmentSlots)
     */
    @Override
    public boolean hasFreePosition(EquipmentSlots equipmentSlot) {
        SlotPositions slots = getSlot(equipmentSlot);
        if (slots != null) {
            return slots.hasFreePosition();
        }
        return false;
    }

    /**
     *
     * @see bg.sarakt.items.inventory.equipment.EquipmentManager#getFirstFreePosition(bg.sarakt.items.inventory.equipment.EquipmentSlots)
     */
    @Override
    public int getFirstFreePosition(EquipmentSlots equipmentSlot) {
        return getSlot(equipmentSlot).getFirstfreePosition();
    }

    /**
     *
     * @see bg.sarakt.items.inventory.equipment.EquipmentManager#getFreePositions(bg.sarakt.items.inventory.equipment.EquipmentSlots)
     */
    public Set<Integer> getFreePositions(EquipmentSlots equipmentSlot) {
        SlotPositions slots = getSlot(equipmentSlot);
        if (slots != null) {
            return slots.getFreePositions(new HashSet<>());
        }
        return Collections.emptySet();
    }
}
