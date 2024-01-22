/*
 * SecondaryAttributesDAO.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate;

import org.springframework.stereotype.Repository;

import bg.sarakt.storing.hibernate.entities.SecondaryAttributeEntity;

@Repository
public class AttributesSecondaryDAO extends AbstractHibernateDAO<SecondaryAttributeEntity> {

    public AttributesSecondaryDAO() {
        super(SecondaryAttributeEntity.class);
    }
}
