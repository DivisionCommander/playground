/*
 * Mythology.java
 *
 * created at 2023-12-06 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.glossary;

public interface Mythology
{

    String getName();

    public record SimpleMythology(String getName)
    {
    };
}