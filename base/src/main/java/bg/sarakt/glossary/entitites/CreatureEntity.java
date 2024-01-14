/*
 * CreatureEntity.java
 *
 * created at 2023-12-06 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.glossary.entitites;

import java.util.Set;

import bg.sarakt.glossary.DefaultMythologies;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_creatures")
public class CreatureEntity
{

    @Id
    @GeneratedValue
    @Column(name = "creature_id", nullable = false, unique = true)
    private long               id;
    @Column(name = "creature_name")
    private String             name;
    @Column(name = "description")
    private String             description;
    @Column(name = "origins")
    private String             origin;
    @Column(name = "origin_mythology")
    @Enumerated(EnumType.STRING)
    private DefaultMythologies mythology;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "list_features", joinColumns = @JoinColumn(name = "feature_id"))
    private Set<String> features;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "map_creatures_tags", joinColumns =
        {
                @JoinColumn(name = "creature_id")
    }, inverseJoinColumns =
        {
                @JoinColumn(name = "tag_id")
    })
    private Set<TagEntity> tags;
    @Column(name = "type")
    private String          type;
    @Column(name = "is_hybrid")
    private boolean         hybrid;
    @Column(name = "is_character")
    private boolean         character;
    @Column(name = "is_unique")
    private boolean         unique;

    public CreatureEntity()
    {
        super();
    }

    public CreatureEntity( String name, String description, String origin, DefaultMythologies mythology, Set<String> features, Set<TagEntity> tags, String type, boolean hybrid, boolean character, boolean unique)
    {
        this();
        this.name = name;
        this.description = description;
        this.origin = origin;
        this.mythology = mythology;
        this.features = features;
        this.tags = tags;
        this.type = type;
        this.hybrid = hybrid;
        this.character = character;
        this.unique = unique;
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getOrigin()
    {
        return origin;
    }

    public DefaultMythologies getMythology()
    {
        return mythology;
    }

    public Set<String> getFeatures()
    {
        return features;
    }

    public Set<TagEntity> getTags()
    {
        return tags;
    }

    public String getType()
    {
        return type;
    }

    public boolean isHybrid()
    {
        return hybrid;
    }

    public boolean isCharacter()
    {
        return character;
    }

    public boolean isUnique()
    {
        return unique;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    public void setMythology(DefaultMythologies mythology)
    {
        this.mythology = mythology;
    }

    public void setFeatures(Set<String> features)
    {
        this.features = features;
    }

    public void setTags(Set<TagEntity> tags)
    {
        this.tags = tags;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setHybrid(boolean hybrid)
    {
        this.hybrid = hybrid;
    }

    public void setCharacter(boolean character)
    {
        this.character = character;
    }

    public void setUnique(boolean unique)
    {
        this.unique = unique;
    }
}
