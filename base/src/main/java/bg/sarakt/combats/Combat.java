/*
 * Combat.java
 *
 * created at 2023-11-24 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.combats;


import bg.sarakt.characters.GameCharacter;


public interface Combat
{
    void prepare();


    void initiate();


    void nextRound();


    Object results();


    CombatState getState();


    GameCharacter winner();

    public enum CombatState
    {
        UNSTARTED,
        PREPARATION,
        INITIATED,
        ONGOING,
        FINISHED;
    }

}
