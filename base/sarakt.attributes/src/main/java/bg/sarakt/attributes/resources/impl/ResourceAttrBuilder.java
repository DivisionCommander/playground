/*
 * ResourceAttrBuilder.java
 *
 * created at 2024-02-07 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.resources.impl;

import java.math.BigDecimal;
import java.util.Map;
import java.util.NavigableMap;
import java.util.StringJoiner;

import bg.sarakt.attributes.internal.AbstractAttributeBuilder;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.resources.ResourceAttribute;
import bg.sarakt.attributes.resources.ResourceAttributeBuilder;

public abstract sealed class ResourceAttrBuilder extends AbstractAttributeBuilder permits ResourceAttributeBuilder {
    
    private PrimaryAttribute                  primary;
    private NavigableMap<Integer, BigDecimal> coefficients;
    
    protected ResourceAttrBuilder setPrimaryAttribute(PrimaryAttribute primaryAttribute) {
        this.primary = primaryAttribute;
        return this;
    }
    
    protected ResourceAttrBuilder addCoefficient(int level, BigDecimal coeffcient) {
        this.coefficients.put(level, coeffcient);
        return this;
    }
    
    protected ResourceAttrBuilder addCoefficients(Map<Integer, BigDecimal> coefficients) {
        this.coefficients.putAll(coefficients);
        return this;
    }
    
    /**
     * @see bg.sarakt.attributes.internal.AbstractAttributeBuilder#doValidate()
     */
    @Override
    protected StringJoiner doValidate() {
        StringJoiner joiner = super.doValidate();
        if (primary == null) {
            joiner.add("No Primary Attribute");
        }
        if (coefficients == null || coefficients.isEmpty()) {
            joiner.add("No coefficients!");
        }
        return joiner;
        
    }
    
    @Override
    public void clear() {
        super.clear();
        this.primary = null;
        this.coefficients.clear();
    }
    
    /**
     * 
     * Construct new {@link ResourceAttribute} from provided data. Note that all
     * fields must be populated.
     * 
     * @see bg.sarakt.attributes.internal.AbstractAttributeBuilder#build()
     */
    @Override
    public final ResourceAttribute build() {
        validate();
        
        ResourceAttributeImpl attribute = new ResourceAttributeImpl(id, name, abbr, group, abbr, primary);
        attribute.addCoefficients(coefficients);
        clear();
        return attribute;
    }
}
