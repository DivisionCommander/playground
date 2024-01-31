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
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.lang.Nullable;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.levels.Level;

public final class PrimaryAttributeMap extends AbstractAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> implements Iterable<PrimaryAttributeEntry> {

    private final PrimaryAttributeEntry[] entries;

    /**
     * Simplified constructor working with default values for
     * {@link PrimaryAttribute}s when constructring their
     * {@link PrimaryAttributeEntry}s
     *
     * @since 0.0.7
     */
    public PrimaryAttributeMap() {
        this((Map<PrimaryAttribute, Number>) null);
    }

    /**
     *
     * @param level
     * @deprecated in favour of {@link PrimaryAttributeMap#PrimaryAttributeMap(Map, Level)}
     */
    @Deprecated(forRemoval =  true)
    public PrimaryAttributeMap(Level level) {
        this(null, level);
    }

    /**
     * Construct new {@link PrimaryAttributeMap}
     *
     *
     * @param valuesArg
     *            if is null, map will be populated with default values;
     * @param level
     * @deprecated dropping support of {@link Level} and
     *             {@link bg.sarakt.characters.Level} as now
     *             {@link CharacterAttributeMap} would manage leveling of
     *             {@link Attribute}s and their {@link AttributeMapEntry}
     */
    @Deprecated(forRemoval = true, since = "0.0.7")
    public PrimaryAttributeMap(@Nullable Map<PrimaryAttribute, Number> valuesArg, Level level) {
        this(valuesArg);
    }

    /**
     * Construct new {@link PrimaryAttributeMap}
     *
     *
     * @param valuesArg
     *            if is null, map will be populated with default values;
     */
    public PrimaryAttributeMap(@Nullable Map<PrimaryAttribute, Number> valuesArg) {
        super();
        entries = new PrimaryAttributeEntry[PrimaryAttribute.count()];
        Map<PrimaryAttribute, Number> values = valuesArg == null ? Collections.emptyMap() : valuesArg;
        for (PrimaryAttribute pa : PrimaryAttribute.getAllPrimaryAttributes()) {
            entries[pa.ordinal()] = new PrimaryAttributeEntry(pa, values.getOrDefault(pa, null));
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

    @Override
    @Deprecated(forRemoval = true, since = "0.0.6")
    public void levelUp() {
        for (int index = 0; index < entries.length; index++) {
            entries[index].levelUp();
        }
    }


    /**
     * @see bg.sarakt.attributes.impl.AttributeMapImpl#get(bg.sarakt.attributes.Attribute)
     */
    @Override
    public PrimaryAttributeEntry get(PrimaryAttribute attr) {
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
            if (index.get() < entries.length) {
                return entries[index.getAndIncrement()];
            }
            throw new NoSuchElementException();
        }

    }

}
