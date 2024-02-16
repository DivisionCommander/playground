/*
 * AbstractEquipmentManager.java
 *
 * created at 2024-01-10 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.items.inventory.equipment.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.base.GameObject;
import bg.sarakt.base.exceptions.InventoryException;
import bg.sarakt.base.utils.ForRemoval;
import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.items.basics.ItemType;
import bg.sarakt.items.basics.Quality;
import bg.sarakt.items.inventory.equipment.Equipment;
import bg.sarakt.items.inventory.equipment.EquipmentManager;
import bg.sarakt.items.inventory.equipment.EquipmentSlots;
import bg.sarakt.items.inventory.equipment.EquipmentView;

public abstract class AbstractEquipmentManager implements EquipmentManager {

    protected final Map<EquipmentSlots, SlotPositions> equipped;
    private final List<AttributeModifier<Attribute>>   modifiers;
    private final Map<Equipment, Set<PositionLock>>    lockedPositions;

    protected AbstractEquipmentManager(Map<EquipmentSlots, Integer> slots) {
        super();
        equipped = new EnumMap<>(EquipmentSlots.class);
        for (Entry<EquipmentSlots, Integer> entry : slots.entrySet()) {
            Integer amount = entry.getValue();
            if (amount != null && amount > 0) {
                SlotPositions slot = new SlotPositions(entry.getValue().intValue());
                equipped.put(entry.getKey(), slot);
            }
        }
        this.modifiers = new ArrayList<>();
        this.lockedPositions = new HashMap<>();
    }

    /**
     *
     * @see bg.sarakt.items.inventory.equipment.EquipmentManager#getBonusAttributes()
     * 
     * @deprecated
     */
    @Override
    @Deprecated(since = "0.0.3", forRemoval = true)
    @ForRemoval(since = "0.0.3", expectedRemovalVersion = "0.0.5")
    public Collection<AttributeValuePair> getBonusAttributes() {
        Map<Attribute, IntSummaryStatistics> collected = modifiers.stream()
                .collect(Collectors.groupingBy(AttributeModifier::getAttribute, Collectors.summarizingInt(m -> m.getValue().intValue())));
        return collected.entrySet().stream().map(e -> new AttributeValuePair(e.getKey(), (int) e.getValue().getSum())).toList();
    }

    /**
     *
     * @see bg.sarakt.items.inventory.equipment.EquipmentManager#canEquip(bg.sarakt.items.inventory.equipment.Equipment)
     */
    @Override
    public boolean canEquip(Equipment eq) {
        SlotPositions slot = getSlot(eq.getSlot());
        if (slot == null) {
            return false;
        }
        if ( !slot.hasFreePosition()) {
            return false;
        }
        Map<EquipmentSlots, Integer> locked = eq.getLockedSlots();
        if (locked == null || locked.isEmpty()) {
            return true;
        }
        return locked.entrySet().stream().allMatch(e -> equipped.get(e.getKey()).hasFreePositions(e.getValue()));
    }

    /**
     * Lock any additional positions for provided piece of equipment (like off-hand
     * for two-handed weapons).
     *
     * @param eq
     * @throws InventoryException
     */
    protected void lockPositions(Equipment eq) throws InventoryException {
        Map<EquipmentSlots, Integer> locked = eq.getLockedSlots();
        if (locked == null || locked.isEmpty()) {
            return;
        }
        Set<PositionLock> positionLocks = new HashSet<>();
        for (Entry<EquipmentSlots, Integer> e : locked.entrySet()) {
            SlotPositions slot = equipped.get(e.getKey());
            Queue<Integer> positions = slot.getFreePositions(new LinkedList<>());
            for (int index = 0; index < e.getValue(); index++) {
                if (positions.isEmpty()) {
                    throw new InventoryException("Not enought position for locks!");
                }
                Integer freePosition = positions.poll();
                PositionLock lock = new PositionLock(e.getKey(), freePosition, eq);
                positionLocks.add(lock);
            }
        }
        lockedPositions.put(eq, positionLocks);
    }

    /**
     * Unlock any locked positions for the provided piece of equipment (like
     * off-hand for two-handed weapons).
     *
     * @param eq
     * @throws InventoryException
     */
    protected void unlockPositions(Optional<Equipment> eq) throws InventoryException {
        if (eq.isEmpty()) {
            return;
        }
        Equipment equipment = eq.get();
        Map<EquipmentSlots, Integer> locked = equipment.getLockedSlots();
        if (locked == null || locked.isEmpty()) {
            return;
        }
        Set<PositionLock> positionLocks = lockedPositions.remove(equipment);
        for (PositionLock lock : positionLocks) {
            SlotPositions slot = equipped.get(lock.slot);
            Optional<Equipment> unequipped = slot.unequip(lock.position);

            if (unequipped.isEmpty() || !unequipped.get().equals(lock)) {
                throw new InventoryException("Illegal position unlocking operation!");
            }
        }
    }

    protected SlotPositions getSlot(EquipmentSlots slot) {
        return equipped.get(slot);
    }

    /**
     * Attempt equipping the {@link Equipment} equipment appointed slot and spot
     * position.
     *
     * @param slot
     * @param position
     * @param equipment
     * @return
     * @throws InventoryException
     */
    protected Optional<Equipment> equip(SlotPositions slot, int position, Equipment equipment) throws InventoryException {
        slot.checkPosition(position);
        Optional<Equipment> oldEquipment = slot.equip(equipment, position);
        removeBonuses(oldEquipment);
        unlockPositions(oldEquipment);
        applyBonuses(equipment);
        lockPositions(equipment);
        return oldEquipment;
    }

    /**
     * Unequip the piece of equipment from provided slot and position.
     *
     * @param slot
     * @param position
     * @return
     * @throws InventoryException
     */
    protected Optional<Equipment> unequip(SlotPositions slot, int position) throws InventoryException {
        Optional<Equipment> eq = slot.unequip(position);
        if (eq.isPresent()) {
            removeBonuses(eq);
            unlockPositions(eq);
        }
        return eq;
    }

    /**
     * Apply any bonuses the equipment may provide.
     *
     * @param eq
     * @return
     */
    protected Equipment applyBonuses(Equipment eq) {
        if (eq != null) {
            modifyBonues(eq, true);
        }
        return eq;
    }

    /**
     * Remove any bonuses the equipment may provide.
     *
     * @param equipment
     * @return
     */
    protected Optional<Equipment> removeBonuses(Optional<Equipment> equipment) {
        if (equipment.isPresent()) {
            modifyBonues(equipment.get(), false);
        }
        return equipment;
    }

    private void modifyBonues(Equipment equipment, boolean apply) {
        if (equipment == null) {
            return;
        }
        List<AttributeModifier<Attribute>> newModifiers = equipment.getModifiers();
        if (newModifiers == null || newModifiers.isEmpty()) {
            return;
        }
        if (apply) {
            this.modifiers.addAll(newModifiers);
        } else {
            this.modifiers.removeAll(newModifiers);
        }
    }

    /**
     * Internal container of all the equipment that can be equipped into the
     * specific slot.
     *
     * @author IceDragon
     */
    protected class SlotPositions {

        private final Equipment[] positions;

        private SlotPositions(int limit) {
            positions = new Equipment[limit];
        }

        protected void swap(int firstPosition, int secondPosition) {

            Equipment temp = positions[firstPosition];
            positions[firstPosition] = positions[secondPosition];
            positions[secondPosition] = temp;
        }

        protected void checkPosition(int position) throws InventoryException {
            if (position < 0 || position > positions.length) {
                throw new InventoryException("Invalid equipment position! Position=" + position);
            }
        }

        protected boolean hasFreePosition() {
            return hasFreePositions(1);
        }

        protected boolean hasFreePositions(int amount) {
            int remaingingPositions = amount;
            for (int index = 0; index < positions.length; index++) {
                if (positions[index] == null) {
                    remaingingPositions--;
                }
                if (remaingingPositions == 0) {
                    return true;
                }
            }
            return false;
        }

        protected <T extends Collection<Integer>> T getFreePositions(T collection) {
            for (int index = 0; index < positions.length; index++) {
                if (positions[index] == null) {
                    collection.add(index);
                }
            }
            return collection;
        }

        protected int getFirstfreePosition() {
            for (int index = 0; index < positions.length; index++) {
                if (positions[index] == null) {
                    return index;
                }
            }
            return -1;
        }

        protected Optional<Equipment> equip(Equipment eq, int position) throws InventoryException {
            checkPosition(position);
            Equipment old = positions[position];
            positions[position] = eq;
            return Optional.ofNullable(old);
        }

        protected Optional<Equipment> unequip(int position) throws InventoryException {
            checkPosition(position);
            Equipment eq = positions[position];
            positions[position] = null;
            return Optional.ofNullable(eq);

        }

        protected List<Optional<Equipment>> removeAll() {
            // Both filter(Objects::isNull) and Optional#ofNullable maybe little redundant.
            // However, needs checking. Stay for now.
            List<Optional<Equipment>> equipment = Arrays.stream(positions).filter(e -> !(e instanceof PositionLock)).filter(Objects::isNull)
                    .map(Optional::ofNullable).toList();

            clear();
            return equipment;
        }

        protected void clear() {
            for (int index = 0; index < positions.length; index++) {
                Equipment eq = positions[index];
                if (eq instanceof PositionLock) {
                    continue;
                }
                positions[index] = null;
            }
        }

        protected List<EquipmentView> convertToView() {
            return Arrays.stream(positions).filter(Objects::nonNull).map(Equipment::getView).toList();
        }
    }

    /**
     * Dummy representation of Equipment used to occupy slot when a complex gear is
     * equipped.
     *
     * @author IceDragon
     */
    private final class PositionLock implements Equipment {

        /** field <code>serialVersionUID</code> */
        private static final long    serialVersionUID = 1L;
        private final EquipmentSlots slot;
        private final int            position;
        private final Equipment      equipment;

        private PositionLock(EquipmentSlots locked, int position, Equipment eq) {
            this.slot = locked;
            this.position = position;
            this.equipment = eq;
        }

        /**
         * @see bg.sarakt.items.inventory.equipment.Equipment#getSlot()
         */
        @Override
        public EquipmentSlots getSlot() { return slot; }

        /**
         * @see bg.sarakt.items.basics.Item#getBasePrice()
         */
        @Override
        public double getBasePrice() { return Double.NaN; }

        /**
         * @see bg.sarakt.items.basics.Item#getName()
         */
        @Override
        public String getName() { return "Locked Position"; }

        /**
         * @see bg.sarakt.items.basics.Item#getType()
         */
        @Override
        public ItemType getType() { return null; }

        /**
         * @see bg.sarakt.items.basics.Item#getQuality()
         */
        @Override
        public Quality getQuality() { return null; }

        /**
         * @see bg.sarakt.base.GameObject#id()
         */
        @Override
        public String id() {
            return "Locked Position";
        }

        /**
         * @see bg.sarakt.base.GameObject#instanceId()
         */
        @Override
        public long instanceId() {
            return equipment.instanceId();
        }

        /**
         * @see bg.sarakt.base.GameObject#equals(bg.sarakt.base.GameObject)
         */
        @Override
        public boolean equals(GameObject gameObject) {
            if (gameObject == null) {
                return false;
            }
            if (gameObject == this) {
                return true;
            }
            if (gameObject instanceof PositionLock lock) {
                return (this.slot == lock.slot && this.position == lock.position);
            }
            if (gameObject instanceof Equipment other) {
                return this.equipment.equals(other);
            }
            if (gameObject instanceof EquipmentView view) {
                return this.equipment.getView().equals(view);
            }
            return false;
        }

        /**
         * @see bg.sarakt.items.inventory.equipment.Equipment#getBonuses()
         * 
         * @deprecated Use {@link Equipment#getModifiers()} instead.
         */
        @Override
        @Deprecated(since = "0.0.3", forRemoval = true)
        @ForRemoval(since = "0.0.3", expectedRemovalVersion = "0.0.5")        
        public Set<AttributeValuePair> getBonuses() { return Collections.emptySet(); }

        /**
         * @see bg.sarakt.items.inventory.equipment.Equipment#getLockedSlots()
         */
        @Override
        public Map<EquipmentSlots, Integer> getLockedSlots() { return Collections.emptyMap(); }


        /**
         * @see bg.sarakt.items.inventory.equipment.Equipment#getView()
         */
        @Override
        public EquipmentView getView() { return null; }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Objects.hash(equipment, position, slot);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if ( !(obj instanceof PositionLock other)) {
                return false;
            }
            if ( !getEnclosingInstance().equals(other.getEnclosingInstance())) {
                return false;
            }
            return Objects.equals(equipment, other.equipment) && position == other.position && slot == other.slot;
        }

        private AbstractEquipmentManager getEnclosingInstance() { return AbstractEquipmentManager.this; }

    }
}
