/*
 * Inventory.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.items.inventory;

import java.io.Serializable;
import java.util.Collection;

import bg.sarakt.items.basics.Item;

public interface Inventory extends Serializable {

    void pickUpItem(Item item);

    void dropItem(Item item);

    Collection<Item> getBackpack();

}
