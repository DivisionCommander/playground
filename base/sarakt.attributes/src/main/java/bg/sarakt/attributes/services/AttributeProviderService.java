/*
 * AttributeProviderService.java
 *
 * created at 2024-02-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.services;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import bg.sarakt.attributes.Attribute;

public interface AttributeProviderService<A extends Attribute> {
    
    int getMaxLevel();
    
    void updateMaxLevel(int newMaxLevel);
    
    void refreshAttributes();
    
    Optional<A> getAttribute(String name);
    
    Map<String, A> getAttributeMap();
    
    Collection<A> getAttributes();
    
    Set<Class<? extends Attribute>> supportedClasses();
}
