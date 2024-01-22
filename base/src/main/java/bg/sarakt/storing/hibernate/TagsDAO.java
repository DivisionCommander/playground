/*
 * TagsDAO.java
 *
 * created at 2023-12-06 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate;

import org.springframework.stereotype.Repository;

import bg.sarakt.glossary.entitites.TagEntity;

@Repository
public class TagsDAO extends AbstractHibernateDAO<TagEntity> {

    public TagsDAO() {
        super(TagEntity.class);
    }

}
