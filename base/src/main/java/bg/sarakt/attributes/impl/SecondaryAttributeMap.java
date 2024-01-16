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

import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.characters.Level;

public class SecondaryAttributeMap extends AbstractAttributeMap<SecondaryAttribute, SecondaryAttributeEntry> {

    private final Set<SecondaryAttribute>                          knownAttributes;
    private final Map<SecondaryAttribute, SecondaryAttributeEntry> entries;

    protected SecondaryAttributeMap(PrimaryAttributeMap primary, Set<SecondaryAttribute> secondary, Level level) {
        super(level);
        knownAttributes = Set.copyOf(secondary);
        entries = new HashMap<>();
        secondary.stream().map(sa -> new SecondaryAttributeEntry(sa, primary, level));
    }

    protected SecondaryAttributeMap(PrimaryAttributeMap primary, SecondaryAttributeMap secondaryAttributesMap, Level level) {
        this(primary, secondaryAttributesMap.knownAttributes, level);
    }

    @Override
    protected SecondaryAttributeEntry getEntry(SecondaryAttribute attr) {
        checkAttribute(attr);
        return this.entries.get(attr);
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
                getEntry(m.getAttribute()).addModifier(m, false);
            } else {
                getEntry(m.getAttribute()).removeModifier(m, false);
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
        getEntry(entry.getKey()).recalculate(entry.getValue());
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMap#getAllValues()
     */
    @Override
    public Map<SecondaryAttribute, BigDecimal> getAllValues() {
        return entries.values().stream().collect(Collectors.toMap(e -> e.getAttribute(), e -> e.getCurrentValue()));
    }

    /**
     * @see bg.sarakt.attributes.ModifiableAttributeMap#levelUp()
     */
    @Override
    public void levelUp() {
        entries.values().forEach(SecondaryAttributeEntry::levelUp);
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMap#levelUp(bg.sarakt.characters.Level)
     */
    @Override
    public void levelUp(Level level) {
        entries.values().forEach(e -> e.levelUp(level));

    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<SecondaryAttributeEntry> iterator() {
        return entries.values().iterator();
    }

}
