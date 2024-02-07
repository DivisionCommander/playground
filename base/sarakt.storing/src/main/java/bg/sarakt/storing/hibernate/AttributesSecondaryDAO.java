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

@Repository("secondaryDao")
@Deprecated(forRemoval = true, since = "0.0.8")
public class AttributesSecondaryDAO extends AbstractHibernateDAO<SecondaryAttributeEntity> {

    private AttributesSecondaryDAO() {
        super();
        setEntityClass(SecondaryAttributeEntity.class);
    }
}
