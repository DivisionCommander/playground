/*
 * InventoryImpl.java
 *
 * created at 2024-01-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.items.inventory.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import bg.sarakt.base.exceptions.InventoryException;
import bg.sarakt.items.basics.Item;
import bg.sarakt.items.inventory.Inventory;
import bg.sarakt.items.inventory.equipment.Equipment;
import bg.sarakt.items.inventory.equipment.EquipmentManager;
import bg.sarakt.logging.Logger;

public class InventoryImpl implements Inventory {

    /** field <code>serialVersionUID</code> */
    private static final long     serialVersionUID          = 202401081213L;
    /** field <code>representationSignatureID</code> */
    protected static final String representationSignatureID = "2024-jan-08-default-inventory";

    private static final Logger LOG = Logger.getLogger();

    protected static boolean AUTOEQUIP = true;

    private EquipmentManager manager;
    private final List<Item> backpack = new ArrayList<>();

    InventoryImpl(EquipmentManager equipmentManager) {
        this.manager = equipmentManager;
    }



    /**
     * @see bg.sarakt.items.inventory.Inventory#pickUpItem(bg.sarakt.items.basics.Item)
     */
    @Override
    public void pickUpItem(Item item) {
        if (AUTOEQUIP && canEquip(item)) {
            attemptEquip(item);
        } else {
            backpack.add(item);
        }
    }

    /**
     * @see bg.sarakt.items.inventory.Inventory#dropItem(bg.sarakt.items.basics.Item)
     */
    @Override
    public void dropItem(Item item) {
        backpack.remove(item);
    }

    /**
     * @see bg.sarakt.items.inventory.Inventory#getBackpack()
     */
    @Override
    public Collection<Item> getBackpack() { return Collections.unmodifiableCollection(backpack); }

    private void attemptEquip(Item item) {
        try {
            if (item instanceof Equipment equipment) {
                int position = manager.getFirstFreePosition(equipment.getSlot());
                Optional<Equipment> oldEquipment = manager.equip(equipment, position);
                if (oldEquipment.isPresent()) {
                    backpack.add(oldEquipment.get());
                }
            }
        } catch (InventoryException e) {
            LOG.error("Cannot equip item " + item);
            e.printStackTrace();
        }
    }

    private boolean canEquip(Item item) {
        if ( !item.isEquippable()) {
            return false;
        }
        if (item instanceof Equipment equipment) {
            return manager.hasFreePosition(equipment.getSlot());
        }
        return false;
    }

}
