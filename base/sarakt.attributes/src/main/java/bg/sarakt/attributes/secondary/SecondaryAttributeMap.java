/*
 * SecondaryAttributeMap.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.secondary;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.internal.AbstractAttributeMap;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.primary.PrimaryAttributeEntry;

public final class SecondaryAttributeMap extends AbstractAttributeMap<SecondaryAttribute, SecondaryAttributeEntry> {

    private final Set<SecondaryAttribute>                          knownAttributes;
    private final Map<SecondaryAttribute, SecondaryAttributeEntry> entries;

    public SecondaryAttributeMap(IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primary, Collection<SecondaryAttribute> secondary) {
        super();
        knownAttributes = Set.copyOf(secondary);
        entries = new HashMap<>();
        secondary.stream().forEach(sa -> entries.put(sa, sa.getEntry(primary)));
    }

    /**
     *
     * @see bg.sarakt.attributes.internal.AbstractAttributeMap#get(bg.sarakt.attributes.Attribute)
     */
    @Override
    public SecondaryAttributeEntry get(SecondaryAttribute attr) {
        checkAttribute(attr);
        return entries.get(attr);
    }

    private void checkAttribute(SecondaryAttribute attr) {
        if ( !knownAttributes.contains(attr)) {
            throw new IllegalArgumentException("Unknown or unsupported attributes!");
        }
    }

    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeMap#changeModifiers(java.util.Collection,
     *      boolean)
     */
    @Override
    protected void changeModifiers(Collection<AttributeModifier<SecondaryAttribute>> modifiers, boolean add) {
        
        Map<SecondaryAttribute, Queue<AttributeModifier<SecondaryAttribute>>> queue = new HashMap<>();
        for (AttributeModifier<SecondaryAttribute> m : modifiers) {
            SecondaryAttribute attribute = m.getAttribute();
            var qu = queue.computeIfAbsent(attribute, e -> new LinkedList<>());
            qu.add(m);
        }
        
        for (var entry : queue.entrySet()) {
            get(entry.getKey()).addModifiers(entry.getValue());
        }
    }


    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeMap#getAllValues()
     */
    @Override
    public Map<SecondaryAttribute, BigDecimal> getAllValues() {
        return entries.values().stream().collect(Collectors.toMap(SecondaryAttributeEntry::getAttribute, SecondaryAttributeEntry::getCurrentValue));
    }


    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<SecondaryAttributeEntry> iterator() {
        return entries.values().iterator();
    }
}
