/*
 * ResourceAttributeEntry.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;

import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.characters.Level;

public  abstract class ResourceAttributeEntry extends AbstractAttributeMapEntry<ResourceAttribute> implements AttributeMapEntry<ResourceAttribute> {

    /**
     * @param attribute
     * @param level
     */
    protected ResourceAttributeEntry(ResourceAttribute attribute, Level level) {
        super(attribute, level);
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBasicValue()
     */
    @Override
    public BigDecimal getBasicValue() { // TODO Auto-generated method stub
    return null; }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBaseValueForLayer(bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    protected BigDecimal getBaseValueForLayer(ModifierLayer layer) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#levelUp(bg.sarakt.characters.Level)
     */
    @Override
    protected void levelUp(Level nextLevel) {

    }

}
