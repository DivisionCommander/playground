/*
 * BiographyImpl.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import bg.sarakt.characters.Biography;

public class BiographyImpl implements Biography {

    /** field <code>serialVersionUID</code> */
    private static final long  serialVersionUID = 202402160140L;
    private final String       characterName;
    /**
     * The first few entries are populated while creating the character. Others are
     * populated during the adventure. Each major decision ave its unique number and
     * value and (usually) cannot overlap. If there are two entries for a concrete
     * index, then addition measures must be taken.
     *
     */
    private final List<String> biography;

    public BiographyImpl(String characterName) {
        this.characterName = characterName;
        this.biography = new ArrayList<>();
    }

    /**
     * @see bg.sarakt.characters.Biography#getCharacterName()
     */
    @Override
    public String getCharacterName() { return characterName; }
    
    /**
     * @see bg.sarakt.characters.Biography#addEntry(int, java.lang.String)
     */
    @Override
    public void addEntry(int position, String value) {
        biography.add(position, value);
    }

    /**
     * @see bg.sarakt.characters.Biography#getFullBiopraphy()
     */
    @Override
    public String getFullBiopraphy() {
        StringJoiner sj = new StringJoiner("\n", "\t", "");
        sj.add("The full biography of " + characterName);
        for (String s : biography) {
            sj.add(s);
        }
        return sj.toString();
    }

    /**
     * @see bg.sarakt.characters.Biography#getShortBiography()
     */
    @Override
    public String getShortBiography() {
        // TEMPORARTY workaround
        return getFullBiopraphy();
    }

}
