/*
 * AttributeService.java
 *
 * created at 2024-02-01 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.attributes.services;

import java.util.Collection;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.resources.ResourceAttribute;
import bg.sarakt.attributes.secondary.SecondaryAttribute;

public interface AttributeService {

    Collection<SecondaryAttribute> defaultSecondaryAttributes();

    Collection<ResourceAttribute> defaultResourceAttributes();

    Collection<SecondaryAttribute> getSecondaryAttributes();

    Collection<ResourceAttribute> getResourceAttributes();

    Attribute ofName(String attributeName);
    
    void loadDefaultAttributes();
    
}


