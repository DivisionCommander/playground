/*
 * UnitClassEntity.java
 *
 * created at 2023-12-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "UnitClass")
@Table(name = "classes")
public class UnitClassEntity implements Serializable
{

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202401291643L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id", unique = true)
    private long   classId;
    @Column(name = "class_name")
    private String className;

    public UnitClassEntity() {}


    public long getClassId() { return classId; }


    public String getClassName() { return className; }


    public void setClassId(long classId) { this.classId = classId; }


    public void setClassName(String className) { this.className = className; }
}
