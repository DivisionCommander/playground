/*
 * Biography.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.characters;


/**
 * Represents the dynamic biography of a character or person. It change when the PC makes decision and world events dynamically populates it
 *
 * @author IceDragon
 */
public interface Biography
{

    void addEntry(int position, String value);


    String getFullBiopraphy();


    String getShortBiography();
}
