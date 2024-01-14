/*
 * GameUnit.java
 *
 * created at 2023-11-24 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters;

import bg.sarakt.base.GameObject;
import bg.sarakt.combats.Combatant;

public interface GameUnit extends GameObject {

    String name();

    boolean isAlive();

    int level();

    Combatant prepareForCombat();
}
