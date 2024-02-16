/*
 * AttributePair.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.characters.attributes;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.base.utils.ForRemoval;

/**
 * @deprecated
 */
@Deprecated(forRemoval = true)
@ForRemoval(expectedRemovalVersion = "0.0.5")
public record AttributeValuePair(Attribute attribute, int value)
{

}



