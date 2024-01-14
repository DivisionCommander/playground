/*
 * AttributeMap.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1.impls;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.characters.attributes1.AttributeMap;

public class AttributeMapImpl implements AttributeMap
{

    private static final int              DEFAULT_VALUE = 9;
    private final Map<Attribute, Integer> attributeMap  = new TreeMap<>(Attribute.getComparator());
    private final Map<Attribute, Integer> modifiers     = new TreeMap<>(Attribute.getComparator());

    public AttributeMapImpl()
    {
        PrimaryAttributes.getAllPrimaryAttributes().forEach(this::setDefaultBasicAttributes);
    }

    public AttributeMapImpl(AttributeMap base)
    {
        base.getAllAtributes().stream().forEach(this::modifyAttribute);
    }

    /**
     * @param attributesForLevel
     */
    public AttributeMapImpl(Set<AttributeValuePair> attributes)
    {
        attributes.stream().forEach(this::setAttribute);
    }

    private void setDefaultBasicAttributes(Attribute a)
    {
        attributeMap.put(a, DEFAULT_VALUE);
    }

    /**
     * @see bg.sarakt.characters.attributes1.AttributeMap#setAttribute(bg.sarakt.characters.attributes.impls.AttributeValuePair)
     */
    @Override
    public AttributeValuePair setAttribute(AttributeValuePair pair)
    {
        Integer oldValue = attributeMap.put(pair.attribute(), pair.value());
        if (oldValue == null)
        {
            oldValue = 0;
        }
        return new AttributeValuePair(pair.attribute(), oldValue);
    }

    /**
     * @see bg.sarakt.characters.attributes1.AttributeMap#setAttributes(java.util.Collection)
     */
    @Override
    public Collection<AttributeValuePair> setAttributes(Collection<AttributeValuePair> pairs)
    {
        return pairs.stream().map(this::setAttribute).collect(Collectors.toSet());
    }

    @Override
    public void modifyAttribute(AttributeValuePair pair)
    {
        Attribute attr = pair.attribute();
        int value = modifiers.getOrDefault(attr, 0);
        value += pair.value();
        modifiers.put(attr, value);
    }

    @Override
    public void modifyAttributes(Collection<AttributeValuePair> pairs)
    {
        pairs.stream().forEach(this::modifyAttribute);
    }

    @Override
    public AttributeValuePair getAttribute(Attribute attr)
    {
        Integer value = attributeMap.getOrDefault(attr, 0);
        value += modifiers.getOrDefault(attr, 0);
        return new AttributeValuePair(attr, value);
    }

    @Override
    public Collection<AttributeValuePair> getAllAtributes()
    {
        return attributeMap.entrySet().stream().map(e -> new AttributeValuePair(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        attributeMap.entrySet().stream().forEach(e -> sb.append(e.getKey()).append('=').append(e.getValue()).append('\t'));

        return sb.toString();

    }

}
