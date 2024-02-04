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
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.ModifiableAttributeMap;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.base.utils.ForRemoval;

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
     * Use
     * {@link ResourceAttributeMap#ResourceAttributeMap(ModifiableAttributeMap, Collection)}
     * 
     * @param resource
     * @param primaryMap
     */
    @Deprecated(forRemoval = true, since = "0.0.11")
    @ForRemoval(since = "0.0.11", expectedRemovalVersion = "0.0.15")
    ResourceAttributeMap(Collection<ResourceAttribute> resource, ModifiableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primaryMap) {
        this(primaryMap, resource);
        
    }

    /**
     *
     * @deprecated dropping support of {@link Level} and
     *             {@link bg.sarakt.characters.Level} as now
     *             {@link CharacterAttributeMap} would manage leveling of
     *             {@link Attribute}s and their {@link AttributeMapEntry}
     */
    @Deprecated(forRemoval =  true, since ="0.0.7")
    ResourceAttributeMap(Level level, Collection<ResourceAttribute> resource, ModifiableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> pa) {
        this(resource, pa);
    }
    
    /**
     *
     * @deprecated dropping support of {@link Level} and
     *             {@link bg.sarakt.characters.Level} as now
     *             {@link CharacterAttributeMap} would manage leveling of
     *             {@link Attribute}s and their {@link AttributeMapEntry}
     */
    @Deprecated(since = "0.0.12", forRemoval = true)
    @ForRemoval(since = "0.0.12", expectedRemovalVersion = "0.0.15")
    ResourceAttributeMap(Collection<ResourceAttribute> resource, Map<PrimaryAttribute, PrimaryAttributeEntry> primaryMap) {
        super();
        this.entries = new HashMap<>();
        for (ResourceAttribute r : resource) {
            PrimaryAttribute pa = r.getPrimaryAttribute();
            entries.put(r, r.getEntry(primaryMap.get(pa)));
        }
    }

    /**
     *
     * @deprecated dropping support of {@link Level} and
     *             {@link bg.sarakt.characters.Level} as now
     *             {@link CharacterAttributeMap} would manage leveling of
     *             {@link Attribute}s and their {@link AttributeMapEntry}
     */
    @Deprecated(forRemoval = true)
    ResourceAttributeMap(Level level, Collection<ResourceAttribute> resource, Map<PrimaryAttribute, PrimaryAttributeEntry> pa) {
        this(resource, pa);
    }

    /**
     * @see bg.sarakt.attributes.AttributeMap#levelUp()
     */
    @Override
    @Deprecated(forRemoval =  true, since = "0.0.6")
    @ForRemoval(since = "0.0.6", expectedRemovalVersion = "0.0.15")
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
                modified.add(entry);
            }
        }
        modified.stream().forEach(ResourceAttributeEntry::recalculate);
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
    
    /**
     * @see bg.sarakt.attributes.ModifiableAttributeMap#setLevel(bg.sarakt.attributes.levels.Level)
     */
    @Override
    public ModifiableAttributeMap<ResourceAttribute, ResourceAttributeEntry> setLevel(Level level) {
        entries.values().forEach(rae -> rae.setLevel(level));
        return this;
    }
}
