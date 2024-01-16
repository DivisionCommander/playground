/*
 * ModifierLayer.java
 *
 * created at 2024-01-14 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.util.Collections;
import java.util.EnumSet;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public enum ModifierLayer
{

    BASELINE_LAYER(2),
    CLASS_LAYER(3),
    GEAR_LAYER(7),
    TEMPORARY_LAYER(19)

    ;

    private static final Set<ModifierLayer>          ALL       = Set.copyOf(EnumSet.allOf(ModifierLayer.class));
    private static final NavigableSet<ModifierLayer> NAVIGABLE = Collections.unmodifiableNavigableSet(new TreeSet<>(ALL));

    private final int position;

    private ModifierLayer(int pos) {
        this.position = pos;
    }

    public int getPosition() { return this.position; }

    public static NavigableSet<ModifierLayer> getNavigableLayers() { return NAVIGABLE; }

    public static NavigableSet<ModifierLayer> getHigherLayers(ModifierLayer layer) {
        return NAVIGABLE.tailSet(layer, true);
    }

    public static Optional<ModifierLayer> getLowerLayer(ModifierLayer layer) {
        ModifierLayer lower = NAVIGABLE.lower(layer);
        return Optional.ofNullable(lower);
    }

    public static Optional<ModifierLayer> getHigherLayer(ModifierLayer layer) {
        ModifierLayer higher = NAVIGABLE.higher(layer);
        return Optional.ofNullable(higher);
    }

    public ModifierLayer checkLower(ModifierLayer other) {
        if (other == null || this == other) {
            return this;
        }
        return this.ordinal() < other.ordinal() ? this : other;
    }

    public ModifierLayer checkHigher(ModifierLayer other) {
        if (other == null || this == other) {
            return this;
        }
        return this.ordinal() > other.ordinal() ? this : other;
    }

    public static ModifierLayer getHighestLayer() { return TEMPORARY_LAYER; }
}
