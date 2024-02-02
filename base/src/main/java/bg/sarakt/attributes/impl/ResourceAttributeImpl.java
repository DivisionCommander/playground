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
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeEntity;

public final class ResourceAttributeImpl extends AbstractAttribute implements ResourceAttribute {

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202401202120L;

    private final PrimaryAttribute                  primary;
    private final NavigableMap<Integer, BigDecimal> coefficients;

    /**
     * @deprecated let provided to take care for conversion;
     */
    @Deprecated(forRemoval = true, since = "0.0.8")
    ResourceAttributeImpl(ResourceAttributeEntity entity){
        this(entity.getId(), entity.getName(), entity.getAbbr(), entity.getGroup(), entity.getDescrption(), entity.getPrimaryAttribute());
    }


    ResourceAttributeImpl(String fullName, String abbreviation, AttributeGroup group, String description, PrimaryAttribute pa) {
        this(System.currentTimeMillis(), fullName, abbreviation, group, description, pa);
    }

    ResourceAttributeImpl(long id, String fullName, String abbreviation, AttributeGroup group, String description, PrimaryAttribute pa) {
        super(id, fullName, abbreviation, group, description);
        this.primary = pa;
        this.coefficients = new TreeMap<>();
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttribute#getId()
     */
    @Override
    public long getId() { return super.getId(); }

    /**
     * @param pa
     * @param level
     * @see bg.sarakt.attributes.ResourceAttribute#getEntry()
     */
    @Override
    @Deprecated(forRemoval =  true, since ="0.0.7")
    public ResourceAttributeEntry getEntry(AttributeMapEntry<PrimaryAttribute> primaryAttributeEntry, Level level) {
        return new ResourceAttributeEntry(this, primaryAttributeEntry, level);
    }

    /**
     *
     * @see bg.sarakt.attributes.ResourceAttribute#getEntry(bg.sarakt.attributes.AttributeMapEntry)
     */
    @Override
    public ResourceAttributeEntry getEntry(AttributeMapEntry<PrimaryAttribute> primaryAttributeEntry) {
        return new ResourceAttributeEntry(this, primaryAttributeEntry);
    }

    /**
     * @see bg.sarakt.attributes.ResourceAttribute#getCoefficientForLevel(bg.sarakt.attributes.levels.Level)
     */
    @Override
    public BigDecimal getCoefficientForLevel(Level level) {
        Entry<Integer, BigDecimal> entry = coefficients.floorEntry(level.getLevelNumber());
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

}
