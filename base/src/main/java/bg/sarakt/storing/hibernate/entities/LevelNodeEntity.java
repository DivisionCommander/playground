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
@Table(name = "tbl_levels_nodes", uniqueConstraints = {
        @UniqueConstraint(name=  "classLevels", columnNames = {"level", "class_id"})
})
public class LevelNodeEntity implements Serializable {

    /** field <code>serialVersionUID</code> */
    @Transient
    private static final long serialVersionUID = 202402040237L;

    @Id
    @GeneratedValue
    @Column(name = "level_id", unique = true)
    private UUID levelId;

    @ManyToOne
    @JoinColumn(name = "level", referencedColumnName = "level", nullable = false)
    private LevelEntity levelEntity;

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

    /**
     * Parameterized constructor for easier storing.
     *
     * @since 0.0.7
     */
    public LevelNodeEntity(PrimaryAttributeValuesEntity primary, List<AdditionalAttrValueEntity> additional) {
        this();
        this.setAdditional(additional);
        this.primary = primary;
    }
    
    public UUID getLevelId() { return levelId; }
    
    @Deprecated(forRemoval =  true, since ="0.0.7")
    public int getLevel() { return levelEntity.getLevel(); }

    public void setLevelId(UUID levelId) { this.levelId = levelId; }

    public PrimaryAttributeValuesEntity getPrimary() { return primary; }

    public void setPrimary(PrimaryAttributeValuesEntity primary) { this.primary = primary; }

    public List<AdditionalAttrValueEntity> getAdditional() { return additional; }

    public void setAdditional(List<AdditionalAttrValueEntity> additional) { this.additional = additional; }

    public UnitClassEntity getUnitClass() { return unitClass; }

    public void setUnitClass(UnitClassEntity unitClass) { this.unitClass = unitClass; }

    public LevelEntity getLevelEntity() {
        return levelEntity;
    }

    public void setLevelEntity(LevelEntity levelEntity) {
        this.levelEntity = levelEntity;
    }

}
