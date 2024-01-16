/*
 * AttributeMap.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1;

import java.util.Collection;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.characters.attributes.AttributeValuePair;

@Deprecated
public interface AttributeMap
{

    AttributeValuePair setAttribute(AttributeValuePair pair);

    Collection<AttributeValuePair> setAttributes(Collection<AttributeValuePair> pairs);

    void modifyAttribute(AttributeValuePair pair);

    void modifyAttributes(Collection<AttributeValuePair> pairs);

    AttributeValuePair getAttribute(Attribute attr);

    Collection<AttributeValuePair> getAllAtributes();

}
