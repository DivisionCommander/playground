/*
 * ModifierLayer.java
 *
 * created at 2024-01-14 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.util.Iterator;
import java.util.Optional;

public enum ModifierLayer
{

    // Usually depends on species (animals, Eg. wolves, bears), race (sentient
    // being, Eg. humans, gnolls, etc. ) and level
    BASELINE_LAYER(2, null)
    {

        @Override
        public Optional<ModifierLayer> higherLayer() {
            return Optional.of(CLASS_LAYER);
        }
    },
    // Changes due to character/unit class
    CLASS_LAYER(3, BASELINE_LAYER)
    {

        /**
         * @see bg.sarakt.attributes.ModifierLayer#higherLayer()
         */
        @Override
        public Optional<ModifierLayer> higherLayer() {
            return Optional.of(GEAR_LAYER);
        }
    },
    // Changes applied by used gear.
    GEAR_LAYER(7, CLASS_LAYER)
    {

        /**
         * @see bg.sarakt.attributes.ModifierLayer#higherLayer()
         */
        @Override
        public Optional<ModifierLayer> higherLayer() {
            return Optional.of(SKIRMISH_LAYER);
        }
    },
    // Temporary bonuses applied only during a combat
    SKIRMISH_LAYER(17, GEAR_LAYER)
    {

        /**
         * @see bg.sarakt.attributes.ModifierLayer#higherLayer()
         */
        @Override
        public Optional<ModifierLayer> higherLayer() {
            return Optional.of(TEMPORARY_LAYER);
        }
    },
    // All additional bonuses applied afterward.
    TEMPORARY_LAYER(19, SKIRMISH_LAYER)
    {

        /**
         * @see bg.sarakt.attributes.ModifierLayer#higherLayer()
         */
        @Override
        public Optional<ModifierLayer> higherLayer() {
            return Optional.empty();
        }
    }

    ;

    private final int               position;
    private Optional<ModifierLayer> previous;

    private ModifierLayer(int pos) {
        this.position = pos;
    }

    private ModifierLayer(int pos, ModifierLayer previous) {
        this.position = pos;
        this.previous = Optional.ofNullable(previous);
    }

    public int getPosition() { return this.position; }

    public Optional<ModifierLayer> lowerLayer() {
        return previous;
    }

    public abstract Optional<ModifierLayer> higherLayer();

    public boolean hasHigherLayer() {
        return higherLayer().isPresent();
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

    public static Iterator<ModifierLayer> getIterator() { return new ModifierLayerIterator(); }

    public static Iterator<ModifierLayer> getIterator(ModifierLayer start) {
        return new ModifierLayerIterator(start);
    }

    public static ModifierLayer getLowestLayer() { return BASELINE_LAYER; }

    public static ModifierLayer getHighestLayer() { return TEMPORARY_LAYER; }

    private static class ModifierLayerIterator implements Iterator<ModifierLayer> {

        private ModifierLayer layer;

        public ModifierLayerIterator() {
            this(ModifierLayer.getLowestLayer());
        }

        public ModifierLayerIterator(ModifierLayer modifierLayer) {
            this.layer = modifierLayer;
        }

        /**
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return layer.hasHigherLayer();
        }

        /**
         * @see java.util.Iterator#next()
         */
        @Override
        public ModifierLayer next() {
            this.layer = layer.higherLayer().get();
            return layer;

        }
    }
}
