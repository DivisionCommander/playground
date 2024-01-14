/*
 * LevelEntity.java
 *
 * created at 2023-12-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Levels")
public class LevelEntity
{

    @Id
    @Column(name = "level", unique = true)
    private int level;

    @Column(name = "experience", unique = true)
    private long xp;

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

}
