/*
 * AttributeService.java
 *
 * created at 2024-02-01 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.attributes;

import java.util.Collection;

public interface AttributeService {

    Collection<SecondaryAttribute> defaultSecondaryAttributes();

    Collection<ResourceAttribute> defaultResourceAttributes();

    Collection<SecondaryAttribute> getSecondaryAttributes();

    Collection<ResourceAttribute> getResourceAttribute();

    Attribute ofName(String attributeName);
    
    void loadDefaultAttributes();
}



