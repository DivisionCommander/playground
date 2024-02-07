/*
 * ResourceAttributeImpl.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.levels.Level;

public final class ResourceAttributeImpl extends AbstractAttribute implements ResourceAttribute {

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202401202120L;

    private final PrimaryAttribute                  primary;
    private final NavigableMap<Integer, BigDecimal> coefficients;


    ResourceAttributeImpl(String fullName, String abbreviation, AttributeGroup group, String description, PrimaryAttribute pa) {
        this(System.currentTimeMillis(), fullName, abbreviation, group, description, pa);
    }

    public ResourceAttributeImpl(long id, String fullName, String abbreviation, AttributeGroup group, String description, PrimaryAttribute pa) {
        super(id, fullName, abbreviation, group, description);
        this.primary = pa;
        this.coefficients = new TreeMap<>();
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttribute#getId()
     */
    @Override
    public long getId() { return super.getId(); }


    @Override
    public ResourceAttributeEntry getEntry(IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> map) {
        return new ResourceAttributeEntryImpl(this, map);
    }

    /**
     * @see bg.sarakt.attributes.ResourceAttribute#getCoefficientForLevel(bg.sarakt.attributes.levels.Level)
     */
    @Override
    public BigDecimal getCoefficientForLevel(Level level) {
        return getCoefficientForLevel(level.getLevelNumber());
    }
    
    @Override
    public BigDecimal getCoefficientForLevel(int levelNumber) {
        Entry<Integer, BigDecimal> entry = coefficients.floorEntry(levelNumber);
        if(entry == null) {
            return BigDecimal.ONE;
        }
        return entry.getValue();
    }

    /**
     * @see bg.sarakt.attributes.ResourceAttribute#getPrimaryAttribute()
     */
    @Override
    public PrimaryAttribute getPrimaryAttribute() { return this.primary; }

    /**
     * @since 0.0.10
     */
    void addCoefficients(NavigableMap<Integer, BigDecimal> coefficients) {
        if (coefficients != null) {
            coefficients.putAll(coefficients);
        }
    }
}
