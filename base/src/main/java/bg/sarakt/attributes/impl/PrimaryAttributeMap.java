/*
 * PrimaryAttributeMap.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.characters.Level;

public final class PrimaryAttributeMap extends AbstractAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> implements Iterable<PrimaryAttributeEntry> {

    private final PrimaryAttributeEntry[] entries;

    private PrimaryAttributeMap(boolean dummy, Level level) {
        super(level);
        entries = new PrimaryAttributeEntry[PrimaryAttribute.count()];
    }

    public PrimaryAttributeMap(Level level) {
        this(false, level);
        for (PrimaryAttribute pa : PrimaryAttribute.getAllPrimaryAttributes()) {
            entries[pa.ordinal()] = new PrimaryAttributeEntry(pa, level);
        }
    }

    public PrimaryAttributeMap(Map<PrimaryAttribute, Number> values, Level level) {
        this(false, level);
        for (PrimaryAttribute pa : PrimaryAttribute.getAllPrimaryAttributes()) {
            entries[pa.ordinal()] = new PrimaryAttributeEntry(pa, values.get(pa), level);
        }
    }

    @Override
    protected void changeModifiers(Collection<AttributeModifier<PrimaryAttribute>> modifiers, boolean add) {
        ModifierLayer[] layerFlag = new ModifierLayer[entries.length];

        for (AttributeModifier<PrimaryAttribute> mod : modifiers) {
            int index = mod.getAttribute().ordinal();
            if (add) {

                entries[index].addModifier(mod, false);
            } else {
                entries[index].removeModifier(mod, false);
            }
            layerFlag[index] = mod.getLayer().checkLower(layerFlag[index]);
        }

        for (int index = 0; index < layerFlag.length; index++) {
            if (layerFlag[index] != null) {
                entries[index].recalculate(layerFlag[index]);
            }
        }
    }

    public void levelUp() {
        for (int index = 0; index < entries.length; index++) {
            entries[index].levelUp();
        }
    }

    /**
     * @see bg.sarakt.attributes.ModifiableAttributeMap#levelUp(bg.sarakt.characters.Level)
     */
    @Override
    public void levelUp(Level level) {
        for (int index = 0; index < entries.length; index++) {
            entries[index].levelUp(level);
        }
    }

    /**
     * @see bg.sarakt.attributes.impl.AttributeMapImpl#getEntry(bg.sarakt.attributes.Attribute)
     */
    @Override
    protected PrimaryAttributeEntry getEntry(PrimaryAttribute attr) {
        return entries[attr.ordinal()];
    }

    /**
     * @see bg.sarakt.attributes.impl.AttributeMapImpl#getAllValues()
     */
    @Override
    public Map<PrimaryAttribute, BigDecimal> getAllValues() {
        Map<PrimaryAttribute, BigDecimal> values = new EnumMap<>(PrimaryAttribute.class);
        for (int index = 0; index < entries.length; index++) {
            PrimaryAttributeEntry entry = entries[index];
            values.put(entry.getAttribute(), entry.getCurrentValue());
        }
        return values;
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<PrimaryAttributeEntry> iterator() {
        return new IteratorImpl();
    }

    private class IteratorImpl implements Iterator<PrimaryAttributeEntry> {

        private final AtomicInteger index = new AtomicInteger(0);

        /**
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return index.get() < entries.length;
        }

        /**
         * @see java.util.Iterator#next()
         */
        @Override
        public PrimaryAttributeEntry next() {
            return entries[index.getAndIncrement()];
        }

    }

}
