/*
 * SecondaryAttributeMap.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.ModifiableAttributeMap;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.attributes.levels.Level;

public final class SecondaryAttributeMap extends AbstractAttributeMap<SecondaryAttribute, SecondaryAttributeEntry> {

    private final Set<SecondaryAttribute>                          knownAttributes;
    private final Map<SecondaryAttribute, SecondaryAttributeEntry> entries;

    SecondaryAttributeMap(IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primary, Collection<SecondaryAttribute> secondary) {
        super();
        knownAttributes = Set.copyOf(secondary);
        entries = new HashMap<>();
        secondary.stream().forEach(sa -> entries.put(sa, sa.getEntry(primary)));
    }

    /**
     *
     * @deprecated dropping support of {@link Level} and
     *             {@link bg.sarakt.characters.Level} as now
     *             {@link CharacterAttributeMap} would manage leveling of
     *             {@link Attribute}s and their {@link AttributeMapEntry}
     */
    @Deprecated(forRemoval = true, since = "0.0.7")
    SecondaryAttributeMap(IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primary, Collection<SecondaryAttribute> secondary,
            Level level) {
        this(primary, secondary);
    }

    /**
     *
     * @see bg.sarakt.attributes.impl.AbstractAttributeMap#get(bg.sarakt.attributes.Attribute)
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
     * @see bg.sarakt.attributes.impl.AbstractAttributeMap#changeModifiers(java.util.Collection,
     *      boolean)
     */
    @Override
    protected void changeModifiers(Collection<AttributeModifier<SecondaryAttribute>> modifiers, boolean add) {
        Map<SecondaryAttribute, ModifierLayer> layers = new HashMap<>();
        for (AttributeModifier<SecondaryAttribute> m : modifiers) {
            if (add) {
                get(m.getAttribute()).addModifier(m, false);
            } else {
                get(m.getAttribute()).removeModifier(m, false);
            }
            ModifierLayer lower = m.getLayer().checkLower(layers.get(m.getAttribute()));
            layers.put(m.getAttribute(), lower);
        }
        layers.entrySet().parallelStream().filter(this::filterEmpty).forEach(this::doRecalculate);
    }

    private boolean filterEmpty(Entry<?, ?> entry) {
        if (entry == null) {
            return false;
        }
        return (entry.getKey() == null || entry.getValue() == null);
    }

    private void doRecalculate(Entry<SecondaryAttribute, ModifierLayer> entry) {
        get(entry.getKey()).recalculate(entry.getValue());
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMap#getAllValues()
     */
    @Override
    public Map<SecondaryAttribute, BigDecimal> getAllValues() {
        return entries.values().stream().collect(Collectors.toMap(SecondaryAttributeEntry::getAttribute, SecondaryAttributeEntry::getCurrentValue));
    }

    /**
     * @see bg.sarakt.attributes.ModifiableAttributeMap#levelUp()
     */
    @Override
    @Deprecated(forRemoval = true, since ="0.0.6")
    public void levelUp() {
        entries.values().forEach(SecondaryAttributeEntry::levelUp);
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<SecondaryAttributeEntry> iterator() {
        return entries.values().iterator();
    }

    /**
     * @see bg.sarakt.attributes.ModifiableAttributeMap#setLevel(bg.sarakt.attributes.levels.Level)
     */
    @Override
    public ModifiableAttributeMap<SecondaryAttribute, SecondaryAttributeEntry> setLevel(Level level) {
        entries.values().forEach(sae -> sae.setLevel(level));
        // TODO Auto-generated method stub
        return this;
    }
}
