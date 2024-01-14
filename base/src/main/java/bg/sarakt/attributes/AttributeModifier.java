/*
 * AttributeModifier.java
 *
 * created at 2024-01-14 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

public interface AttributeModifier<A extends Attribute> {

    A getAttribute();

    <T extends Number> T getValue();

    ModifierType getBonusType();

    ModifierLayer getLayer();
}
