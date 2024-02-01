/*
 * AttributesResourceDAO.java
 *
 * created at 2024-01-21 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate;

import org.springframework.stereotype.Repository;

import bg.sarakt.storing.hibernate.entities.ResourceAttributeEntity;

@Repository
@Deprecated(forRemoval = true, since = "0.0.8")
public class AttributesResourceDAO extends AbstractHibernateDAO<ResourceAttributeEntity> {

    public AttributesResourceDAO() {
        super();
        setEntityClass(ResourceAttributeEntity.class);
    }
}
