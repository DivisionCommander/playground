/*
 * Stackable.java
 *
 * created at 2024-01-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.items.basics;

public interface StackableItem extends Item {

    void addToStack();

    StackableItem drawFromStack();

    int currentStackSize();

    int maxStackSize();

    default boolean isFull() { return maxStackSize() <= currentStackSize(); }
}
