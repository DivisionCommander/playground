/*
 * AbstractAttributeMap.java
 *
 * created at 2024-01-14 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.base.Pair;
import bg.sarakt.base.Pair.PairImpl;
import bg.sarakt.base.exceptions.SaraktRuntimeException;

public abstract class AbstractAttributeMap<T extends Attribute> {

    private static final NavigableSet<ModifierLayer> LAYERS = Collections.unmodifiableNavigableSet(ModifierLayer.getNavigableLayers());
    private final Set<T>                             allAttributes;

    // Immutable collection of initial attributes;
    private final Map<T, Integer> initialAttributes;
    // modifiersPerLayer
    private final Map<ModifierLayer, Map<T, List<ModPair>>> modifiersPerLevel;
    // Calculated values per level after applying modifiers;
    protected final NavigableMap<ModifierLayer, Map<T, Number>> calculatedValues;

    protected AbstractAttributeMap(Map<T, Integer> attributes) {
        this.initialAttributes = Map.copyOf(attributes);
        this.allAttributes = Set.copyOf(attributes.keySet());
        this.calculatedValues = new TreeMap<>();
        this.modifiersPerLevel = new EnumMap<>(ModifierLayer.class);

        LAYERS.parallelStream().forEach(l ->
        {
            Map<T, List<ModPair>> mapPairs = new HashMap<>();
            Map<T, Number> mapCalculate = new HashMap<>();
            for (T attr : attributes.keySet()) {
                mapPairs.put(attr, new LinkedList<>());
                mapCalculate.put(attr, 0);

            }
            modifiersPerLevel.put(l, mapPairs);
            calculatedValues.put(l, mapCalculate);
        });
    }

    private Map<T, Number> getInitialValuesPerLayer(ModifierLayer layer) {
        Map<T, Number> initialValues;
        if (layer == ModifierLayer.BASELINE_LAYER) {
            initialValues = Collections.unmodifiableMap(initialAttributes);
        } else {
            initialValues = calculatedValues.lowerEntry(layer).getValue();
        }
        // FIXME: handle null values
        return initialValues;
    }

    protected void clear() {
        modifiersPerLevel.values().parallelStream().forEach(map -> map.values().parallelStream().forEach(List::clear));
    }

    protected boolean addModifier(AttributeModifier<T> modificator) {
        Map<T, List<ModPair>> map = modifiersPerLevel.get(modificator.getLayer());
        map.putIfAbsent(modificator.getAttribute(), new LinkedList<>());
        ModPair pair = new ModPair(modificator.getBonusType(), modificator.getValue());
        return map.get(modificator.getAttribute()).add(pair);
    }

    protected boolean removeModifier(AttributeModifier<T> modificator) {
        List<ModPair> list = modifiersPerLevel.get(modificator.getLayer()).get(modificator.getAttribute());
        ModPair modPair = new ModPair(modificator.getBonusType(), modificator.getValue());

        if (list == null || list.isEmpty() || !list.contains(modPair)) {
            throw new IllegalArgumentException("No such modificator applied!");
        }
        return list.remove(modPair);
    }

    protected void calculate(ModifierLayer layer) {
        Iterator<ModifierLayer> it = LAYERS.tailSet(layer, true).iterator();

        while (it.hasNext()) {
            ModifierLayer modLayer = it.next();
            Map<T, Number> initialValues = getInitialValuesPerLayer(modLayer);
            Map<T, List<ModPair>> modifiers = modifiersPerLevel.get(modLayer);
            Map<T, Number> newValues = recalculate(initialValues, modifiers);

            Map<T, Number> values = calculatedValues.get(modLayer);
            values.clear();
            values.putAll(newValues);
        }
    }

    private Map<T, Number> recalculate(Map<T, Number> initial, Map<T, List<ModPair>> mods) {
        if ( !initial.keySet().equals(mods.keySet())) {
            throw new SaraktRuntimeException();
        }
        return allAttributes.parallelStream().map(at -> recalc(at, initial.get(at), mods.get(at)))
                .collect(Collectors.toMap(p -> p.left(), p -> p.right()));
    }

    private Pair<T, Number> recalc(T attribute, Number initialValue, List<ModPair> mods) {
        Number n = recalculate(attribute, initialValue, mods);
        return new PairImpl<>(attribute, n);
    }

    protected abstract Number recalculate(T attribute, Number initialValue, List<ModPair> mods);

    protected abstract Map<T, Integer> getAllAttributes();

    private record ModPair(ModifierType left, Number right) implements Pair<ModifierType, Number> {
    }

    private class Modifier {

        private final T attribute;
        private Number  value;

        protected Modifier(T attributeArg, Number valueArg) {
            this.attribute = attributeArg;
            this.value = valueArg;
        }

        @Override
        public int hashCode() {
            return attribute.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            return this.attribute.equals(other);
        }
    }
}
