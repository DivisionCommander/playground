/*
 * CharacterAttributeMap.java
 *
 * created at 2024-01-31 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.attributes;

import java.math.BigInteger;

public interface CharacterAttributeMap extends AttributeMap<Attribute> {

    void earnExperience(BigInteger amount);
}



