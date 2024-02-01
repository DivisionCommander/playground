/*
 * PrimaryAttrValuesDAO.java
 *
 * created at 2024-01-29 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate;

import static bg.sarakt.attributes.impl.PrimaryAttribute.*;

import java.math.BigInteger;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;
import org.springframework.stereotype.Repository;

import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.storing.hibernate.entities.PrimaryAttributeValuesEntity;

@Repository
public class PrimaryAttrValuesDAO extends AbstractHibernateDAO<PrimaryAttributeValuesEntity> {

    public PrimaryAttrValuesDAO() {
        super();
        setEntityClass(PrimaryAttributeValuesEntity.class);
    }

    public PrimaryAttributeValuesEntity get(Map<PrimaryAttribute, BigInteger> map) {
        Session session = getCurrentSession();

        String hql = "FROM PrimaryAttributeValues"
                     + " WHERE strength=:STRENGTH"
                     + " AND agility=:AGILITY"
                     + " AND constitution=:CONSTITUTION"
                     + " AND intelligence=:INTELLIGENCE"
                     + " AND wisdom=:WISDOM"
                     + " AND psionic=:PSIONIC"
                     + " AND spirit=:SPIRIT"
                     + " AND will=:WILL";

        SelectionQuery<PrimaryAttributeValuesEntity> query = session.createSelectionQuery(hql, PrimaryAttributeValuesEntity.class);
        query.setParameter(STRENGTH.name(), map.get(STRENGTH));
        query.setParameter(AGILITY.name(), map.get(AGILITY));
        query.setParameter(CONSTITUTION.name(), map.get(CONSTITUTION));
        query.setParameter(INTELLIGENCE.name(), map.get(INTELLIGENCE));
        query.setParameter(WISDOM.name(), map.get(WISDOM));
        query.setParameter(PSIONIC.name(), map.get(PSIONIC));
        query.setParameter(SPIRIT.name(), map.get(SPIRIT));
        query.setParameter(WILL.name(), map.get(WILL));
        return query.getSingleResultOrNull();
    }
}
