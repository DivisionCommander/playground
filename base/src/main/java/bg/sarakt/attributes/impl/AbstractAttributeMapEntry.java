/*
 * AbstractAttributeMapEntry.java
 *
 * created at 2024-01-14 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.base.Pair;
import bg.sarakt.base.exceptions.SaraktRuntimeException;
import bg.sarakt.characters.Level;
import bg.sarakt.logging.Logger;

public abstract class AbstractAttributeMapEntry<T extends Attribute> implements AttributeMapEntry<T> {

    private static final boolean  USE_OLD = false;
    protected static final Logger LOG     = Logger.getLogger();

    protected Level                                                 level;
    protected final T                                               attr;
    private final ConcurrentNavigableMap<ModifierLayer, BigDecimal> valuesPerLayer;
    private final Map<ModifierLayer, List<Modifier>>                modifiers;

    protected AbstractAttributeMapEntry(T attribute, Level level) {
        Objects.requireNonNull(attribute, "Attribute cannot be null!");
        this.attr = attribute;
        this.level = level;
        valuesPerLayer = new ConcurrentSkipListMap<>(new EnumMap<>(ModifierLayer.class));
        modifiers = new ConcurrentHashMap<>(new EnumMap<>(ModifierLayer.class));
        ModifierLayer.getNavigableLayers().stream().forEach(l ->
        {
            modifiers.put(l, new ArrayList<>());
            valuesPerLayer.put(l, BigDecimal.ZERO);
        });
    }

    @Override
    public final T getAttribute() { return this.attr; }

    @Override
    public void addModifier(AttributeModifier<T> modifier) {
        addModifier(modifier, true);
    }

    @Override
    public void addModifiers(Collection<AttributeModifier<T>> modifiers) {
        if (modifiers == null || modifiers.isEmpty()) {
            // TODO: some better handling;
            return;
        }
        ModifierLayer layer = ModifierLayer.TEMPORARY_LAYER;
        for (AttributeModifier<T> mod : modifiers) {
            addModifier(mod, false);
            layer = layer.checkLower(mod.getLayer());
        }
        recalculate(layer);
    }

    @Override
    public void removeModifiers(Collection<AttributeModifier<T>> modifiers) {
        if (modifiers == null || modifiers.isEmpty()) {
            // TODO: some better handling;
            return;
        }
        ModifierLayer layer = ModifierLayer.TEMPORARY_LAYER;
        for (AttributeModifier<T> mod : modifiers) {
            removeModifier(mod, false);
            layer = layer.checkLower(mod.getLayer());
        }
        recalculate(layer);
    }

    protected void addModifier(AttributeModifier<T> modifier, boolean recalculate) {
        modifiers.get(modifier.getLayer()).add(new Modifier(modifier));
        if (recalculate) {
            recalculate(modifier.getLayer());
        }
    }

    @Override
    public void removeModifier(AttributeModifier<T> modifier) {
        removeModifier(modifier, true);
    }

    protected void removeModifier(AttributeModifier<T> modifier, boolean recalculate) {
        List<Modifier> modifiersPerLayer = modifiers.get(modifier.getLayer());
        Modifier pair = new Modifier(modifier);
        if ( !modifiersPerLayer.contains(pair)) {
            throw new SaraktRuntimeException();
        }
        modifiersPerLayer.remove(pair);
        if (recalculate) {
            recalculate(modifier.getLayer());
        }
    }

    @Override
    public void replaceModifier(AttributeModifier<T> old, AttributeModifier<T> newM) {
        boolean recalculate = false;
        if (old != null) {
            removeModifier(old, false);
            recalculate = true;
        }
        if (newM != null) {
            addModifier(newM, false);
            recalculate = true;
        }
        if (recalculate) {
            ModifierLayer layer = old.getLayer().checkLower(newM.getLayer());
            recalculate(layer);
        }
    }

    @Override
    public abstract BigDecimal getBasicValue();

    @Override
    public BigDecimal getValueForLayer(ModifierLayer layer) {
        return valuesPerLayer.get(layer);
    }

    @Override
    public BigDecimal getCurrentValue() { return valuesPerLayer.get(ModifierLayer.getHighestLayer()); }

    protected void recalculate() {
        recalculate(ModifierLayer.BASELINE_LAYER);
    }

    protected void recalculate(ModifierLayer layer) {
        recalculate(layer, getBaseValueForLayer(layer));
    }

    protected void recalculate(ModifierLayer layer, BigDecimal baseValue) {
        Iterator<ModifierLayer> iterator = ModifierLayer.getHigherLayers(layer).iterator();
        while (iterator.hasNext()) {
            ModifierLayer next = iterator.next();
            BigDecimal result = applyModifiers(baseValue, modifiers.get(next));
            valuesPerLayer.put(layer, result);
        }
    }

    protected abstract BigDecimal getBaseValueForLayer(ModifierLayer layer);

    protected BigDecimal applyModifiers(BigDecimal baseValue, List<Modifier> mods) {
        BigDecimal flat = BigDecimal.ZERO;
        BigDecimal coefficient = BigDecimal.ZERO;

        for (Modifier mod : mods) {
            Number value = mod.right();
            BigDecimal bdValue;
            switch (mod.left())
            {
            case FLAT_VALUE:
                bdValue = (value instanceof BigDecimal bdv) ? bdv : BigDecimal.valueOf(value.intValue());
                flat = flat.add(bdValue);
                break;

            case COEFFICIENT:
                bdValue = (value instanceof BigDecimal bdv) ? bdv : BigDecimal.valueOf(value.doubleValue());
                coefficient = coefficient.add(bdValue);
                break;

            default:
                throw new IllegalArgumentException("Unknown modifier type!");
            }
        }

        LOG.debug("Flat bonus = " + flat);
        LOG.debug("Modifier Coefficient =" + coefficient);

        if (USE_OLD) {
            // deprecated formula
            return applyModifiers(baseValue, flat, coefficient);
        }
        return applyModifiers0(baseValue, flat, coefficient);
    }

    private BigDecimal applyModifiers0(BigDecimal baseValue, BigDecimal flat, BigDecimal coefficient) {

        // result = (base * coefficient) + flat + base;
        BigDecimal result = baseValue.multiply(coefficient);
        result = result.add(flat);
        result = result.add(baseValue);

        return result;
    }

    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#levelUp()
     */
    @Override
    public void levelUp() {
        levelUp(level.getNextLevel());
    }

    protected abstract void levelUp(Level nextLevel);

    /**
     * Considering formula to be selectable via settings, may need later.
     *
     * @param baseValue
     * @param flat
     * @param coefficient
     * @return
     */
    @Deprecated(forRemoval = false)
    private BigDecimal applyModifiers(BigDecimal baseValue, BigDecimal flat, BigDecimal coefficient) {

        // result =(coef* flat) + int + flat;
        BigDecimal result = flat.multiply(coefficient);
        result = flat.add(result);
        result = baseValue.add(result);
        return result;
    }

    @Override
    public int hashCode() {
        return attr.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (other instanceof AttributeMapEntry<?> entry) {
            return this.getAttribute().equals(entry.getAttribute());
        }
        if (other instanceof Attribute attribute) {
            return this.getAttribute().equals(attribute);
        }
        return false;
    }

    protected record Modifier(ModifierType left, Number right) implements Pair<ModifierType, Number> {

        protected Modifier(AttributeModifier<?> mod) {
            this(mod.getBonusType(), mod.getValue());
        }
    }
}
