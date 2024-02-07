/*
 * TagEntity.java
 *
 * created at 2023-12-06 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.glossary.entitites;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Tags")
@Table(name = "tbl_tags")
public class TagEntity implements Serializable {

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202312062100L;

    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private long   id;
    @Column(name = "tag_body", unique = true)
    private String tag;

    public TagEntity() {
        super();
    }

    public long getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return tag + "#" + id;
    }
}
