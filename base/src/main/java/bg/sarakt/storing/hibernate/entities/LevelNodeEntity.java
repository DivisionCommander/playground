/*
 * LevelNodeEntity.java
 *
 * created at 2024-01-27 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

@Entity(name = "LevelNode")
@Table(name = "tbl_levels", uniqueConstraints = {
        @UniqueConstraint(name=  "classLevels", columnNames = {"level", "class_id"})
})
public class LevelNodeEntity implements Serializable {

    /** field <code>serialVersionUID</code> */
    @Transient
    private static final long serialVersionUID = 202401271952L;

    @Id
    @GeneratedValue
    @Column(name = "level_id", unique = true)
    private UUID levelId;
    @Column(name = "level", unique = false)
    private int  level;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private UnitClassEntity unitClass;

    @JoinColumn(name = "values_id")
    @Column(name = "primary_attributes", length = 1000)
    private PrimaryAttributeValuesEntity primary;

    @ManyToMany
    @JoinTable(name = "map_additional_attributes", joinColumns =
        {
                @JoinColumn(name = "level_node_id", referencedColumnName = "level_id")
    }, inverseJoinColumns =
        {
                @JoinColumn(name = "attribute_value_id", referencedColumnName = "pair_id")
    })
    private List<AdditionalAttrValueEntity> additional;

    public LevelNodeEntity() {
        setAdditional(new ArrayList<>());
    }

    public LevelNodeEntity(int level) {
        this();
        this.level = level;
    }

    public LevelNodeEntity(int level, List<AdditionalAttrValueEntity> additional) {
        this(level);
        this.setAdditional(additional);
    }

    public LevelNodeEntity(int level, PrimaryAttributeValuesEntity primary, List<AdditionalAttrValueEntity> additional) {
        this(level, additional);
        this.primary = primary;
    }

    public UUID getLevelId() { return levelId; }

    public int getLevel() { return level; }

    public void setLevelId(UUID levelId) { this.levelId = levelId; }

    public void setLevel(int level) { this.level = level; }

    public PrimaryAttributeValuesEntity getPrimary() { return primary; }

    public void setPrimary(PrimaryAttributeValuesEntity primary) { this.primary = primary; }

    public List<AdditionalAttrValueEntity> getAdditional() { return additional; }

    public void setAdditional(List<AdditionalAttrValueEntity> additional) { this.additional = additional; }

    public UnitClassEntity getUnitClass() { return unitClass; }

    public void setUnitClass(UnitClassEntity unitClass) { this.unitClass = unitClass; }

}