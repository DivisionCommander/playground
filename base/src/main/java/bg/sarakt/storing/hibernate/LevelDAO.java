/*
 * LevelDAO.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate;

import java.util.NavigableMap;
import java.util.TreeMap;

import bg.sarakt.storing.hibernate.entities.LevelEntity;
import bg.sarakt.storing.hibernate.interfaces.ILevelDAO;

import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class LevelDAO extends AbstractHibernateDAO<LevelEntity> implements ILevelDAO {
    
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
    
    @Override
    public NavigableMap<Integer, LevelEntity> allAsMap() {
        NavigableMap<Integer, LevelEntity> levels = new TreeMap<>();
        for (LevelEntity level : findAll()) {
            levels.put(level.getLevel(), level);
        }
        return levels;
    }
    
    @Override
    public int getMaxlevel() {
        String hql = "Select max(level) FROM " + clazz.getName();
        
        SelectionQuery<Integer> query = getCurrentSession().createSelectionQuery(hql, Integer.class);
        Integer result = query.getSingleResultOrNull();
        return result == null ? -1 : result;
    }
    
}
