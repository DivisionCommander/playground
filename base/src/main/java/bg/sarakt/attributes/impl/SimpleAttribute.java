/*
 * SimpleAttribute.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.attributes.impl;


import bg.sarakt.attributes.Attribute;


public record SimpleAttribute(String fullName, String abbreviation, AttributeType type, String description) implements Attribute
{
    @Override
    public String toString()
    {
        return fullName;
    }

}
