/*
 * ResourceAttributeBuilder.java
 *
 * created at 2024-02-10 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.resources;

import java.math.BigDecimal;
import java.util.Map;

import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.resources.impl.ResourceAttrBuilder;

public final class ResourceAttributeBuilder extends ResourceAttrBuilder {
    
    /**
     * @see bg.sarakt.attributes.resources.impl.ResourceAttrBuilder#setId(long)
     */
    @Override
    public ResourceAttributeBuilder setId(long id) {
        super.setId(id);
        return this;
    }
    
    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeBuilder#setName(java.lang.String)
     */
    @Override
    public ResourceAttributeBuilder setName(String name) {
        super.setName(name);
        return this;
    }
    
    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeBuilder#setAbbreviation(java.lang.String)
     */
    @Override
    public ResourceAttributeBuilder setAbbreviation(String abbreviation) {
        super.setAbbreviation(abbreviation);
        return this;
    }
    
    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeBuilder#setDescription(java.lang.String)
     */
    @Override
    public ResourceAttributeBuilder setDescription(String description) {
        super.setDescription(description);
        return this;
    }
    
    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeBuilder#setGroup(java.lang.String)
     */
    @Override
    public ResourceAttributeBuilder setGroup(String groupName) {
        super.setGroup(groupName);
        return this;
    }
    
    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeBuilder#setGroup(bg.sarakt.attributes.AttributeGroup)
     */
    @Override
    public ResourceAttributeBuilder setGroup(AttributeGroup group) {
        super.setGroup(group);
        return this;
    }
    
    /**
     * @see bg.sarakt.attributes.resources.impl.ResourceAttrBuilder#setPrimaryAttribute(bg.sarakt.attributes.primary.PrimaryAttribute)
     */
    @Override
    public ResourceAttributeBuilder setPrimaryAttribute(PrimaryAttribute primaryAttribute) {
        super.setPrimaryAttribute(primaryAttribute);
        return this;
    }
    
    /**
     * @see bg.sarakt.attributes.resources.impl.ResourceAttrBuilder#addCoefficient(int,
     *      java.math.BigDecimal)
     */
    @Override
    public ResourceAttributeBuilder addCoefficient(int level, BigDecimal coeffcient) {
        super.addCoefficient(level, coeffcient);
        return this;
    }
    
    /**
     * @see bg.sarakt.attributes.resources.impl.ResourceAttrBuilder#addCoefficients(java.util.Map)
     */
    @Override
    public ResourceAttributeBuilder addCoefficients(Map<Integer, BigDecimal> coefficients) {
        super.addCoefficients(coefficients);
        return this;
    }
}