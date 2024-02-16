/*
 * Biography.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.characters;

import java.io.Serializable;

/**
 * Represents the dynamic biography of a character or person. It change when the
 * PC makes decision and world events dynamically populates it. Later,
 * biographical entries will be converted from string to stand-alone entries to
 * help tracking what happens or is done by this character.
 *
 */
public interface Biography extends Serializable
{
    
    /**
     * 
     * @since 0.0.3
     */
    String getCharacterName();

    void addEntry(int position, String value);


    String getFullBiopraphy();


    String getShortBiography();
}
