/*
 * ModifierLayer.java
 *
 * created at 2024-01-14 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.util.EnumSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public enum ModifierLayer
{

    BASELINE_LAYER(2),
    CLASS_LAYER(3),
    GEAR_LAYER(7),
    TEMPORARY_LAYER(19)

    ;

    private static final Set<ModifierLayer> ALL = Set.copyOf(EnumSet.allOf(ModifierLayer.class));

    private final int position;

    private ModifierLayer(int pos) {
        this.position = pos;
    }

    public int getPosition() { return this.position; }

    public static NavigableSet<ModifierLayer> getNavigableLayers() { return new TreeSet<>(ALL); }
}
