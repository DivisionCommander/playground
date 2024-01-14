/*
 * AttributeFormula.java
 *
 * created at 2023-11-29 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1;

import java.util.Map;

import bg.sarakt.attributes.Attribute;

@FunctionalInterface
public interface AttributeFormula
{

    Number calculate(Map<Attribute, Number> attributes);
}
