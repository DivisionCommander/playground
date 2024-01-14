/*
 * DefaultMythologies.java
 *
 * created at 2023-12-06 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.glossary;

public enum DefaultMythologies implements Mythology
{

    GREEK("Greek"),
    NORSE("Norse"),
    CHINA("Chinese"),
    JAPAN("Japanese"),
    EGYPT("Egyptian"),

    OTHER("Other")
    ;

    private final String name;

    private DefaultMythologies(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return this.name;

    }
}
