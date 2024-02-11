/*
 * PrimaryAttributeMap.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.primary;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.internal.AbstractAttributeMap;
import bg.sarakt.attributes.levels.Level;

import org.springframework.lang.Nullable;

public final class PrimaryAttributeMap extends AbstractAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> implements Iterable<PrimaryAttributeEntry> {

    private final PrimaryAttributeEntry[] entries;

    /**
     * Simplified constructor working with default values for
     * {@link PrimaryAttribute}s when constructing their
     * {@link PrimaryAttributeEntry}s
     *
     * @since 0.0.7
     */
    public PrimaryAttributeMap() {
        this(null, null);
    }

    /**
     * Construct new {@link PrimaryAttributeMap}
     * 
     * Introducing of the {@link PrimaryAttribute#EXPERIENCE} requires the actual
     * level structure. However, if it is not provided
     * 
     * @param levelArg
     *            if not provided will use default
     * @param attrValues
     */
    public PrimaryAttributeMap(@Nullable Level levelArg, @Nullable Map<PrimaryAttribute, Number> attrValues) {
        super();
        Level level = levelArg == null ? Level.DEFAULT_LEVEL : levelArg;
        entries = new PrimaryAttributeEntry[PrimaryAttribute.count()];
        var values = attrValues == null ? defaultValues() : attrValues;
        for (var e : values.entrySet()) {
            PrimaryAttribute pa = e.getKey();
            var pae = pa == PrimaryAttribute.EXPERIENCE ? new ExperienceEntryImpl(e.getValue(), level) : pa.getEntry(e.getValue());
            pae.recalculate();
            entries[pa.ordinal()] = pae;
        }
    }
    
    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeMap#addModifier(bg.sarakt.attributes.AttributeModifier)
     */
    @Override
    public void addModifier(AttributeModifier<PrimaryAttribute> modifier) {
        if (modifier.getBonusType() == ModifierType.PRIMARY_PERMANENT) {
            entries[modifier.getAttribute().ordinal()].addPermanentBonus(modifier.getValue());
            return;
        }
        super.addModifier(modifier);
    }
    
    @Override
    protected void changeModifiers(Collection<AttributeModifier<PrimaryAttribute>> modifiers, boolean add) {
        ModifierWrapper[] mods = new ModifierWrapper[entries.length];
        for (var mod : modifiers) {
            int index = mod.getAttribute().ordinal();
            if (mods[index] == null) {
                mods[index] = new ModifierWrapper();
            }
            mods[index].add(mod);
        }
        for (int index = 0; index < entries.length; index++) {
            var mod = mods[index];
            if (mod != null && !mod.isEmpty()) {
                if (add) {
                    entries[index].addModifiers(mod.mods);
                } else {
                    entries[index].removeModifiers(mod.mods);
                }
            }
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
    
    
    public ExperienceEntryImpl getExperienceEntry() {
        PrimaryAttributeEntry primaryAttributeEntry = get(PrimaryAttribute.EXPERIENCE);
        if (primaryAttributeEntry instanceof ExperienceEntryImpl ee) {
            return ee;
        }
        return null;
        
    }
    
    public static Map<PrimaryAttribute, Number> defaultValues() {
        Map<PrimaryAttribute, Number> map = new EnumMap<>(PrimaryAttribute.class);
        for (PrimaryAttribute pa : PrimaryAttribute.getAllPrimaryAttributes()) {
            map.put(pa, PrimaryAttributeEntry.getDefaultValue());
        }
        map.put(PrimaryAttribute.EXPERIENCE, BigInteger.ZERO);
        return map;
    }
    
    private class ModifierWrapper {
        
        private Collection<AttributeModifier<PrimaryAttribute>> mods = new LinkedList<>();
        
        private boolean add(AttributeModifier<PrimaryAttribute> mod) {
            return this.mods.add(mod);
        }
        
        public boolean isEmpty() { return mods.isEmpty(); }
    }
}