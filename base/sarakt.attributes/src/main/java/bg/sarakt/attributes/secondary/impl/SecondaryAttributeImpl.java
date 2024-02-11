/*
 * SecondaryAttributeImpl.java
 *
 * created at 2023-11-29 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.secondary.impl;

import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.internal.AbstractAttribute;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.primary.PrimaryAttributeEntry;
import bg.sarakt.attributes.secondary.SecondaryAttribute;
import bg.sarakt.attributes.secondary.SecondaryAttributeEntry;

public final class SecondaryAttributeImpl extends AbstractAttribute implements SecondaryAttribute
{

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202401202125L;


    private final NavigableMap<Integer, AttributeFormula> formulas = new TreeMap<>();


    SecondaryAttributeImpl(long id, String fullName, String abbreviation, AttributeGroup group, String description) {
        super(id, fullName, abbreviation, group, description);
    }
    
    SecondaryAttributeImpl(String fullName, String abbreviation, AttributeGroup type, String description) {
        this(System.currentTimeMillis(), fullName, abbreviation, type, description);
    }

    /**
     * @see bg.sarakt.attributes.internal.AbstractAttribute#getId()
     */
    @Override
    public long getId() { return super.getId(); }

    void addFormula(int level, AttributeFormula formula) {
        formulas.put(level, formula);
    }


    /**
     * @see bg.sarakt.attributes.secondary.SecondaryAttribute#getFormula()
     */
    @Override
    public AttributeFormula getFormula(int level) {
        return formulas.floorEntry(level).getValue();
    }

    void putFormulas(SortedMap<Integer, AttributeFormula> formulas) {
        this.formulas.putAll(formulas);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(getId()).append(']').append(fullName()).append(" -[").append(abbreviation()).append("]-\t").append(group()).append(group().ordinal());

        return sb.toString();
    }

    @Override
    public SecondaryAttributeEntry getEntry(IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primaryAttribute) {
        return new SecondaryAttributeEntryImpl(this, primaryAttribute);
    }

    /**
     * @see bg.sarakt.attributes.internal.AbstractAttribute#hashCode()
     */
    @Override
    public int hashCode() {
        int prime = 41;
        Long result = 0L;
        result += super.hashCode();
        result = prime * result + abbreviation().hashCode();
        return result.intValue();
    }
    
    /**
     * @see bg.sarakt.attributes.internal.AbstractAttribute#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SecondaryAttribute sa) {
            return this.fullName().equalsIgnoreCase(sa.fullName()) && this.getId() == sa.getId();
        }
        return false;
    }
    
}