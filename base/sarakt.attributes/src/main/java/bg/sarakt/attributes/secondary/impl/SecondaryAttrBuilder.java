/*
 * SecondaryAttributeBuilder.java
 *
 * created at 2024-02-07 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.secondary.impl;

import java.util.Map;
import java.util.SortedMap;
import java.util.StringJoiner;
import java.util.TreeMap;

import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.internal.AbstractAttributeBuilder;
import bg.sarakt.attributes.secondary.SecondaryAttribute;
import bg.sarakt.attributes.secondary.SecondaryAttributeBuilder;

/**
 * Serves as as proxy to access to <package> private methods of
 * {@link SecondaryAttributeImpl}
 */
public abstract sealed class SecondaryAttrBuilder extends AbstractAttributeBuilder permits SecondaryAttributeBuilder {
    
    private final SortedMap<Integer, AttributeFormula> formulas;
    
    protected SecondaryAttrBuilder() {
        formulas = new TreeMap<>();
    }
    
    protected SecondaryAttrBuilder addFormula(int level, AttributeFormula formula) {
        if (level > 0 && formula != null) {
            formulas.put(level, formula);
        }
        return this;
    }
    
    protected SecondaryAttrBuilder addFormulas(Map<Integer, AttributeFormula> formulas) {
        if (formulas != null) {
            this.formulas.putAll(formulas);
        }
        return this;
    }
    
    @Override
    public StringJoiner doValidate() {
        StringJoiner validator = super.doValidate();
        if (formulas == null || formulas.isEmpty()) {
            validator.add("No formulas!");
        }
        return validator;
    }
    
    @Override
    public void clear() {
        super.clear();
        this.formulas.clear();
    }
    
    /**
     * Construct new {@link SecondaryAttribute} from provided data. Note that all
     * fields must be populated.
     * 
     * @see bg.sarakt.attributes.internal.AbstractAttributeBuilder#build()
     */
    @Override
    public final SecondaryAttribute build() {
        validate();
        SecondaryAttributeImpl attr = new SecondaryAttributeImpl(id, name, abbr, group, description);
        attr.putFormulas(formulas);
        clear();
        return attr;
    }
}