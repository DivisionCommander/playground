/*
 * Pair.java
 *
 * created at 2023-12-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.base;

public interface Pair<A, B>
{

    A left();

    B right();

    public static record PairImpl<A, B> (A left, B right) implements Pair<A, B>
    {
    }

}
