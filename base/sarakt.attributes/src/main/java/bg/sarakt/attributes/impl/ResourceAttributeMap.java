/*
 * ResourceAttributeMap.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifiableAttributeMap;
import bg.sarakt.attributes.ResourceAttribute;

public final class ResourceAttributeMap extends AbstractAttributeMap<ResourceAttribute, ResourceAttributeEntry> {

    private final Map<ResourceAttribute, ResourceAttributeEntry> entries;


    ResourceAttributeMap(ModifiableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primaryMap, Collection<ResourceAttribute> resource) {
        super();
        this.entries = new HashMap<>();
        for (ResourceAttribute r : resource) {
            entries.put(r, r.getEntry(primaryMap));
        }
    }
    

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMap#get(bg.sarakt.attributes.Attribute)
     */
    @Override
    public ResourceAttributeEntry get(ResourceAttribute attr) {
        return entries.get(attr);
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMap#changeModifiers(java.util.Collection,
     *      boolean)
     */
    @Override
    protected void changeModifiers(Collection<AttributeModifier<ResourceAttribute>> modifiers, boolean add) {
        Map<ResourceAttribute, List<AttributeModifier<ResourceAttribute>>> toAdd = new HashMap<>();
        Map<ResourceAttribute, List<AttributeModifier<ResourceAttribute>>> toRemove = new HashMap<>();
        for (AttributeModifier<ResourceAttribute> m : modifiers) {
            if (add) {
                List<AttributeModifier<ResourceAttribute>> list = toAdd.get(m.getAttribute());
                if (list == null) {
                    list = new LinkedList<>();
                    toAdd.put(m.getAttribute(), list);
                }
                list.add(m);
            } else {
                List<AttributeModifier<ResourceAttribute>> list = toRemove.get(m.getAttribute());
                if (list == null) {
                    list = new LinkedList<>();
                    toAdd.put(m.getAttribute(), list);
                }
                list.add(m);
            }
        }
        for (var e : toRemove.entrySet()) {
            entries.get(e.getKey()).removeModifiers(e.getValue());
        }
        
        for (var e : toAdd.entrySet()) {
            entries.get(e.getKey()).addModifiers(e.getValue());
        }
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMap#getAllValues()
     */
    @Override
    public Map<ResourceAttribute, BigDecimal> getAllValues() {
        return entries.entrySet().stream().collect(Collectors.toMap(Entry::getKey, e->e.getValue().getCurrentValue()));
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMap#iterator()
     */
    @Override
    public Iterator<ResourceAttributeEntry> iterator() {
        return entries.values().iterator();
    }
    
}
