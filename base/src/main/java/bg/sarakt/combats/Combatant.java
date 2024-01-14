/*
 * Combatant.java
 *
 * created at 2023-11-24 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.combats;


public interface Combatant
{

    void defend(Attack attack);


    boolean isAlive();
}
