/*
 * LevelEntity.java
 *
 * created at 2023-12-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity(name = "Level")
@Table(name = "tbl_levels")
public class LevelEntity implements Serializable
{

    /** field <code>serialVersionUID</code> */
    @Transient
    private static final long serialVersionUID = -6584892784131956865L;

    @Id
    @Column(name = "level", unique = true)
    private int level;

    @Column(name = "experience", unique = true)
    private long xp;

    @Column(name = "free_points", unique = false)
    private Integer freePoints = 5;

    public LevelEntity()
    {}

    public LevelEntity(long xp, int level)
    {
        this.level = level;
        this.xp = xp;
    }

    public int getLevel()
    {
        return level;
    }

    public long getXp()
    {
        return xp;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public void setXp(long xp)
    {
        this.xp = xp;
    }

    public Integer getFreePoints() {
        return freePoints;
    }

    public void setFreePoints(Integer freePoints) {
        if (freePoints != null) {
            this.freePoints = freePoints;
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LevelEntity [level=" + this.level + ", xp=" + this.xp + ", freePoints=" + this.freePoints + "]";
    }
    
}
