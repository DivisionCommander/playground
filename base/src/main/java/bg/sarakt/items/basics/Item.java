/*
 * Item.java
 *
 * created at 2023-11-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.items.basics;

import bg.sarakt.base.GameObject;

public interface Item extends GameObject {

    String getName();

    double getBasePrice();

    ItemType getType();

    Quality getQuality();

    boolean isEquippable();

    ItemView getView();

}
