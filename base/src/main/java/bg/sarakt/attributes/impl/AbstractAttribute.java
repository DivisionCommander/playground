/*
 * AbstractAttribute.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.io.Serializable;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeGroup;

public abstract sealed class AbstractAttribute implements Attribute, Serializable permits ResourceAttributeImpl, SecondaryAttributeImpl {

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202401202119L;

    private final long           id;
    private final String         fullName;
    private final String         abbreviation;
    private final AttributeGroup group;
    private final String         description;

    protected AbstractAttribute(long id, String fullName, String abbreviation, AttributeGroup group, String description) {
        super();
        this.id = id;
        this.fullName = fullName;
        this.abbreviation = abbreviation;
        this.group = group;
        this.description = description;
    }

    /**
     * @see bg.sarakt.attributes.Attribute#fullName()
     */
    @Override
    public String fullName() {
        return fullName;
    }

    /**
     * @see bg.sarakt.attributes.Attribute#abbreviation()
     */
    @Override
    public String abbreviation() {
        return abbreviation;
    }

    /**
     *
     * @see bg.sarakt.attributes.Attribute#group()
     */
    @Override
    public AttributeGroup group() {
        return group;
    }

    /**
     *
     * @see bg.sarakt.attributes.Attribute#description()
     */
    @Override
    public String description() {
        return description;
    }

    protected long getId() { return id; }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(id).append(']').append(fullName).append(" -[").append(abbreviation).append("]-\t").append(group);
        if (group != null) {
            sb.append("[").append(group.ordinal()).append("]");
        }
        return sb.toString();
    }

}
