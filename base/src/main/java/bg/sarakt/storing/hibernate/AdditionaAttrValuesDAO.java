/*
 * AdditionaAttrValuesDAO.java
 *
 * created at 2024-01-29 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate;

import java.math.BigDecimal;

import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;
import org.springframework.stereotype.Repository;

import bg.sarakt.storing.hibernate.entities.AdditionalAttrValueEntity;

@Repository
public class AdditionaAttrValuesDAO extends AbstractHibernateDAO<AdditionalAttrValueEntity> {

    public AdditionaAttrValuesDAO() {
        super();
        setEntityClass( AdditionalAttrValueEntity.class);
    }

    public AdditionalAttrValueEntity get(String attr, BigDecimal value) {
        String hql = "FROM AdditionalAttributeValues"
                    + " WHERE attribute=:attr"
                    + " AND value=:value";

        Session session = getCurrentSession();
        SelectionQuery<AdditionalAttrValueEntity> query = session.createSelectionQuery(hql, clazz);
        query.setParameter("attr", attr);
        query.setParameter("value", value);

        return query.getSingleResultOrNull();
    }

}
