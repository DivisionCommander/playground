/*
 * AbstractAttributeBuilder.java
 *
 * created at 2024-02-07 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.internal;

import java.util.StringJoiner;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.resources.impl.ResourceAttrBuilder;
import bg.sarakt.attributes.secondary.impl.SecondaryAttrBuilder;

public abstract sealed class AbstractAttributeBuilder permits ResourceAttrBuilder, SecondaryAttrBuilder {
    
    protected Long           id;
    protected String         name;
    protected String         abbr;
    protected String         description;
    protected AttributeGroup group;
    
    protected AbstractAttributeBuilder setId(long id) {
        this.id = id;
        return this;
    }
    protected AbstractAttributeBuilder setName(String name) {
        this.name = name;
        return this;
    }
    
    protected AbstractAttributeBuilder setAbbreviation(String abbreviation) {
        this.abbr = abbreviation;
        return this;
    }
    
    protected AbstractAttributeBuilder setDescription(String description) {
        this.description = description;
        return this;
    }
    
    protected AbstractAttributeBuilder setGroup(String groupName) {
        AttributeGroup groupArg = AttributeGroup.resolve(groupName);
        if (groupArg != null) {
            this.group = groupArg;
        }
        return this;
    }
    
    protected AbstractAttributeBuilder setGroup(AttributeGroup group) {
        this.group = group;
        return this;
    }
    
    protected StringJoiner doValidate() {
        
        StringJoiner validator = new StringJoiner(" ");
        if (id == null) {
            validator.add("Missing ID");
        }
        if (name == null) {
            validator.add("Missing name");
        }
        if (abbr == null) {
            validator.add("Missing abbreviation.");
        }
        if (description == null) {
            validator.add("Missing description.");
        }
        if (group == null) {
            validator.add("Missing group");
        }
        return validator;
    }
    
    protected void clear() {
        id= null;
        name = null;
        abbr = null;
        description = null;
        group = null;
    }
    
    public final boolean isAttributeValid() {
        StringJoiner validator = doValidate();
        return validator.length() == 0;
    }
    
    protected final void validate() {
        StringJoiner joiner = doValidate();
        if (joiner.length() != 0) {
            throw new IllegalArgumentException(joiner.toString());
        }
    }
    protected abstract Attribute build();
}
