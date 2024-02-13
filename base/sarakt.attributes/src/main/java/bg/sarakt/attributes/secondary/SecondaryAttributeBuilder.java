/*
 * SecondaryAttributeBuilder.java
 *
 * created at 2024-02-07 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.secondary;

import java.util.Map;

import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.secondary.impl.SecondaryAttrBuilder;

public final class SecondaryAttributeBuilder extends SecondaryAttrBuilder {
    
    @Override
    public SecondaryAttributeBuilder setId(long id) {
        super.setId(id);
        return this;
    }
    
    /**
     * 
     * @see bg.sarakt.attributes.internal.AbstractAttributeBuilder#setName(java.lang.String)
     */
    @Override
    public SecondaryAttributeBuilder setName(String name) {
        super.setName(name);
        return this;
    }
    
    /**
     * 
     * @see bg.sarakt.attributes.internal.AbstractAttributeBuilder#setAbbreviation(java.lang.String)
     */
    @Override
    public SecondaryAttributeBuilder setAbbreviation(String abbreviation) {
        super.setAbbreviation(abbreviation);
        return this;
    }
    
    /**
     * 
     * @see bg.sarakt.attributes.internal.AbstractAttributeBuilder#setDescription(java.lang.String)
     */
    @Override
    public SecondaryAttributeBuilder setDescription(String description) {
        super.setDescription(description);
        return this;
    }
    
    /**
     * 
     * @see bg.sarakt.attributes.internal.AbstractAttributeBuilder#setGroup(java.lang.String)
     */
    @Override
    public SecondaryAttributeBuilder setGroup(String groupName) {
        super.setGroup(groupName);
        return this;
    }
    
    /**
     * 
     * @see bg.sarakt.attributes.internal.AbstractAttributeBuilder#setGroup(bg.sarakt.attributes.AttributeGroup)
     */
    @Override
    public SecondaryAttributeBuilder setGroup(AttributeGroup group) {
        super.setGroup(group);
        return this;
    }
    
    @Override
    public SecondaryAttributeBuilder addFormula(int level, AttributeFormula formula) {
        super.addFormula(level, formula);
        return this;
    }
    
    @Override
    public SecondaryAttributeBuilder addFormulas(Map<Integer, AttributeFormula> formulas) {
        super.addFormulas(formulas);
        return this;
    }
}