/*
 * SimpleAttribute.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.attributes.impl;


import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeGroup;

@Deprecated(forRemoval = true)
public record SimpleAttribute(String fullName, String abbreviation, AttributeGroup group, String description) implements Attribute
{
    @Override
    public String toString()
    {
        return fullName;
    }

}
