/*
 * SimpleInventoryImpl.java
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

import bg.sarakt.items.basics.Item;
import bg.sarakt.items.inventory.Inventory;

public class SimpleInventoryImpl implements Inventory {

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID          = 202401080129L;
    /** field <code>representationSignatureID</code> */
    static final String       representationSignatureID = "2024-jan-08-simple-inventory";

    private final List<Item> backpack;

    public SimpleInventoryImpl() {
        // super(representationSignatureID);
        this.backpack = new ArrayList<>();
    }

    /**
     * @see bg.sarakt.items.inventory.Inventory#pickUpItem(bg.sarakt.items.basics.Item)
     */
    @Override
    public void pickUpItem(Item item) {
        if (item == null) {
            return;
        }
        backpack.add(item);
    }

    /**
     * @see bg.sarakt.items.inventory.Inventory#dropItem(bg.sarakt.items.basics.Item)
     */
    @Override
    public void dropItem(Item item) {
        if (backpack.contains(item)) {
            backpack.remove(item);
        }
    }

    /**
     * @see bg.sarakt.items.inventory.Inventory#getBackpack()
     */
    @Override
    public Collection<Item> getBackpack() { return Collections.unmodifiableCollection(backpack); }
}
