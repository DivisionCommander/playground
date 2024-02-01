/*
 * LevelDAO.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import bg.sarakt.storing.hibernate.entities.LevelEntity;

@Repository
public class LevelDAO extends AbstractHibernateDAO<LevelEntity> {

    public LevelDAO() {
        super();
        setEntityClass(LevelEntity.class);
    }

    public LevelEntity get(int level) {

        String hql = "FROM " + clazz + " WHERE level=:level";
        Session session = getCurrentSession();
        var query = session.createSelectionQuery(hql, clazz).setParameter("level", level);
        return query.getSingleResult();
    }

    public LevelEntity get(LevelEntity entity) {
        return get(entity.getLevel());
    }
}
