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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifiableAttributeMap;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.characters.Level;

public class ResourceAttributeMap extends AbstractAttributeMap<ResourceAttribute, ResourceAttributeEntry> {

    private final Map<ResourceAttribute, ResourceAttributeEntry> entries;


    ResourceAttributeMap(Level level, Collection<ResourceAttribute> resource, ModifiableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> pa) {
        super();
        this.entries = new HashMap<>();
        resource.stream().forEach(r -> entries.put(r, r.getEntry(pa.get(r.getPrimaryAttribute()), level)));
    }

    ResourceAttributeMap(Level level, Collection<ResourceAttribute> resource, Map<PrimaryAttribute, PrimaryAttributeEntry> pa) {
        super();
        this.entries = new HashMap<>();
        resource.stream().forEach(r -> entries.put(r, r.getEntry(pa.get(r.getPrimaryAttribute()), level)));
    }

    /**
     * @see bg.sarakt.attributes.AttributeMap#levelUp()
     */
    @Override
    @Deprecated(forRemoval =  true, since = "0.0.6")
    public void levelUp() {
        entries.values().stream().forEach(ResourceAttributeEntry::levelUp);

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
        Set<ResourceAttributeEntry> modified = new HashSet<>();
        for (AttributeModifier<ResourceAttribute> m : modifiers) {
            ResourceAttributeEntry entry = entries.get(m.getAttribute());
            if (add) {
                entry.addModifier(m, false);
                modified.add(entry);
            } else {
                entry.removeModifier(m, false);
            }
        }
        modified.stream().forEach(ResourceAttributeEntry::recalculate);
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMap#getAllValues()
     */
    @Override
    public Map<ResourceAttribute, BigDecimal> getAllValues() {
        return entries.entrySet().stream().collect(Collectors.toMap(e->e.getKey(), e->e.getValue().getCurrentValue()));
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMap#iterator()
     */
    @Override
    public Iterator<ResourceAttributeEntry> iterator() {
        return entries.values().iterator();
    }
}
