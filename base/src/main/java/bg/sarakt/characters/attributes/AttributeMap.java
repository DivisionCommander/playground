/*
 * AttributeMap.java
 *
 * created at 2024-01-12 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes;

import java.util.Map;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.impl.PrimaryAttribute;

@Deprecated
public interface AttributeMap {

    void addBonus(AttributeBonus bonus);

    void removeBonus(AttributeBonus bonus);

    Integer getAttributeValue(Attribute attribute);

    Map<PrimaryAttribute, Integer> getBaseAttributes();

    Map<Attribute, Integer> getAllAttributes();

}
