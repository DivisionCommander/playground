/*
 * AbstractAttributeMap.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifiableAttributeMap;
import bg.sarakt.attributes.ModifierLayer;

public abstract class AbstractAttributeMap<A extends Attribute, E extends AttributeMapEntry<A>> implements ModifiableAttributeMap<A, E> {

    AbstractAttributeMap() {}

    @Override
    public abstract E get(A attr);


    /**
     * considering @{link AttributeMap} extends {@link Map} with lock immediately after populating the map.
    * @formatter:off
    public boolean isEmpty() {return false;}
    public abstract size();
    public abstract containsKey(Object key);
    public boolean containsValue(Object value) {throw new UnsupportedOperationException();}
    public E put(A key, E value) {throw new UnsupportedOperationException();}
    public E remove (Object key) {throw new UnsupportedOperationException();}
    public void putAll(Map<? extends A, ? extends E> map) {throw new UnsupportedOperationException();}
    public void clear() {throw new UnsupportedOperationException();}
    public abstract Set<A> keySet();
    public abstract Collection<E> values();
    public Abstract Set<Entry<A, E>> entrySet();
      @formatter:on
    */

    protected abstract void changeModifiers(Collection<AttributeModifier<A>> modifiers, boolean add);

    /**
     *
     * @see bg.sarakt.attributes.AttributeMap#getAllValues()
     */
    @Override
    public abstract Map<A, BigDecimal> getAllValues();

    /**
     *
     * @see bg.sarakt.attributes.ModifiableAttributeMap#addModifier(bg.sarakt.attributes.AttributeModifier)
     */
    @Override
    public void addModifier(AttributeModifier<A> modifier) {
        get(modifier.getAttribute()).addModifier(modifier);
    }

    /**
     *
     * @see bg.sarakt.attributes.ModifiableAttributeMap#addModifiers(java.util.Collection)
     */
    @Override
    public void addModifiers(Collection<AttributeModifier<A>> modifiers) {
        changeModifiers(modifiers, false);
    }

    /**
     *
     * @see bg.sarakt.attributes.ModifiableAttributeMap#removeModifier(bg.sarakt.attributes.AttributeModifier)
     */
    @Override
    public void removeModifier(AttributeModifier<A> modifier) {
        get(modifier.getAttribute()).removeModifier(modifier);
    }

    /**
     *
     * @see bg.sarakt.attributes.ModifiableAttributeMap#removeModifiers(java.util.Collection)
     */
    @Override
    public void removeModifiers(Collection<AttributeModifier<A>> modifiers) {
        changeModifiers(modifiers, false);
    }

    /**
     * @see bg.sarakt.attributes.ModifiableAttributeMap#getAttributeValueForLayer(bg.sarakt.attributes.Attribute,
     *      bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    public BigDecimal getAttributeValueForLayer(A attribute, ModifierLayer layer) {
        return get(attribute).getValueForLayer(layer);
    }

    /**
     *
     * @see bg.sarakt.attributes.AttributeMap#getCurrentAttributeValue(bg.sarakt.attributes.Attribute)
     */
    @Override
    public BigDecimal getCurrentAttributeValue(A attribute) {
        return get(attribute).getCurrentValue();
    }

    /**
     *
     * @see bg.sarakt.attributes.AttributeMap#getBaseValue(bg.sarakt.attributes.Attribute)
     */
    @Override
    public BigDecimal getBaseValue(A attribute) {
        return get(attribute).getBaseValue();
    }

    protected class IteratorWrapper<I extends AttributeMapEntry<A>> implements Iterator<AttributeMapEntry<A>> {

        private Iterator<I> it;

        protected IteratorWrapper(Iterator<I> iterator) {
            Objects.requireNonNull(iterator);
            this.it = iterator;
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public AttributeMapEntry<A> next() {
            return it.next();
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return it.hashCode();
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            return it.equals(obj);
        }
    }

    protected abstract class Node implements Entry<A, E>{

        /**
         * @see java.util.Map.Entry#getKey()
         */
        @Override
        public A getKey() { // TODO Auto-generated method stub
        return null; }

        /**
         * @see java.util.Map.Entry#getValue()
         */
        @Override
        public E getValue() { // TODO Auto-generated method stub
        return null; }

        /**
         * @see java.util.Map.Entry#setValue(java.lang.Object)
         */
        @Override
        public E setValue(E value) {
            throw new UnsupportedOperationException();
        }

    }
}
