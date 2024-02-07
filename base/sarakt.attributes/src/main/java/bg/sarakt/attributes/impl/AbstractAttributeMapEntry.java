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
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.base.Pair;
import bg.sarakt.base.exceptions.SaraktRuntimeException;
import bg.sarakt.logging.Logger;

public abstract sealed class AbstractAttributeMapEntry<T extends Attribute> implements AttributeMapEntry<T>
        permits PrimaryAttributeEntry, ResourceAttributeEntryImpl, SecondaryAttributeEntry {

    protected static final Logger LOG     = Logger.getLogger();

    protected final T                                                   attr;
    private final ConcurrentNavigableMap<ModifierLayer, BigDecimal>     valuesPerLayer;
    private final ConcurrentNavigableMap<ModifierLayer, List<Modifier>> modifiers;

    protected AbstractAttributeMapEntry(T attribute) {
        Objects.requireNonNull(attribute, "Attribute cannot be null!");
        this.attr = attribute;
        valuesPerLayer = new ConcurrentSkipListMap<>(new EnumMap<>(ModifierLayer.class));
        modifiers = new ConcurrentSkipListMap<>(new EnumMap<>(ModifierLayer.class));

        Iterator<ModifierLayer> it = ModifierLayer.getIterator();
        while (it.hasNext()) {
            ModifierLayer layer = it.next();
            modifiers.put(layer, new ArrayList<>());
            valuesPerLayer.put(layer, BigDecimal.ZERO);
        }
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
        ModifierLayer layer = ModifierLayer.getHighestLayer();
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
        ModifierLayer layer = ModifierLayer.getHighestLayer();
        for (AttributeModifier<T> mod : modifiers) {
            removeModifier(mod, false);
            layer = layer.checkLower(mod.getLayer());
        }
        recalculate(layer);
    }

    @Override
    public void removeModifier(AttributeModifier<T> modifier) {
        removeModifier(modifier, true);
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
            ModifierLayer layer = ModifierLayer.checkLower(old.getLayer(), newM.getLayer()).get();
            recalculate(layer);
        }
    }

    @Override
    public abstract BigDecimal getBaseValue();

    protected abstract BigDecimal getBaseValueForLayer(ModifierLayer layer);

    @Override
    public BigDecimal getValueForLayer(ModifierLayer layer) {
        return valuesPerLayer.get(layer);
    }
    
    protected final BigDecimal currentValue() {
        return valuesPerLayer.get(ModifierLayer.getHighestLayer());
    }
    
    @Override
    public BigDecimal getCurrentValue() { return valuesPerLayer.get(ModifierLayer.getHighestLayer()); }
    
    protected void addModifier(AttributeModifier<T> modifier, boolean recalculate) {
        modifiers.get(modifier.getLayer()).add(new Modifier(modifier));
        if (recalculate) {
            recalculate(modifier.getLayer());
        }
    }

    protected void removeModifier(AttributeModifier<T> modifier, boolean recalculate) {
        List<Modifier> modifiersPerLayer = modifiers.get(modifier.getLayer());
        Modifier pair = new Modifier(modifier);
        if ( !modifiersPerLayer.contains(pair)) {
            // FIXME: better handling
            throw new SaraktRuntimeException();
        }
        modifiersPerLayer.remove(pair);
        if (recalculate) {
            recalculate(modifier.getLayer());
        }
    }

    @Override
    public void recalculate() {
        BigDecimal old = currentValue();
        recalculate(ModifierLayer.getLowestLayer(), getBaseValue());
        BigDecimal newV= currentValue();
        LOG.debug("[" + getClass().getSimpleName() + "\t" + getAttribute().fullName() + "] changes from {" + old + "} to {" + newV + "}");
    }

    protected void recalculate(ModifierLayer layer) {
        recalculate(layer, getBaseValueForLayer(layer));
    }

    protected void recalculate(ModifierLayer layerArg, BigDecimal baseValue) {
        Optional<ModifierLayer> optionalLayer = Optional.of(layerArg);
        while (optionalLayer.isPresent()) {
            ModifierLayer layer = optionalLayer.get();
            BigDecimal result = applyModifiers(baseValue, modifiers.get(layer));
            valuesPerLayer.put(layer, result);
            optionalLayer = layer.higherLayer();
        }

    }
    
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

        return applyModifiers0(baseValue, flat, coefficient);
    }

    private BigDecimal applyModifiers0(BigDecimal baseValue, BigDecimal flat, BigDecimal coefficient) {

        // result = (base * coefficient) + flat + base;
        BigDecimal result = baseValue.multiply(coefficient);
        result = result.add(flat);
        result = result.add(baseValue);

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
    
    @Override
    public String toString() {
        return getClass().getSimpleName()
               + "[Attribute="
               + attr.fullName()
               + ", valuesPerLayer="
               + valuesPerLayer
               + ", modifiers="
               + modifiers
               + "]";
    }
    
}
