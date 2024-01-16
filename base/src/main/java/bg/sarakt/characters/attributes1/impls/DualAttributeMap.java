/*
 * DualAttributeMap.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1.impls;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.characters.attributes1.AttributeMap;

public abstract class DualAttributeMap implements AttributeMap
{

    protected final EnumMap<PrimaryAttribute, Integer> basicAttributes;
    protected final Map<Attribute, Integer>           additionalAttributes;

    public DualAttributeMap( )
    {
        this.basicAttributes = new EnumMap<>(PrimaryAttribute.class);
        additionalAttributes = new TreeMap<>(Attribute.getComparator( ));
    }

    /**
     * @see bg.sarakt.characters.attributes1.AttributeMap#getAllAtributes()
     */
    @Override
    public Collection<AttributeValuePair> getAllAtributes( )
    {

        // TODO Auto-generated method stub
        return null;
    }



}
