/*
 * LevelDAO.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate;

import java.math.BigInteger;
import java.util.NavigableMap;
import java.util.TreeMap;

import bg.sarakt.storing.hibernate.entities.LevelEntity;
import bg.sarakt.storing.hibernate.interfaces.ILevelDAO;

import org.hibernate.query.SelectionQuery;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LevelDAO extends AbstractHibernateDAO<LevelEntity> implements ILevelDAO {
    
    public LevelDAO() {
        super();
        setEntityClass(LevelEntity.class);
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
        String hql = "SELECT max(level) FROM " + clazz.getName();
        
        SelectionQuery<Integer> query = getCurrentSession().createSelectionQuery(hql, Integer.class);
        Integer result = query.getSingleResultOrNull();
        return result == null ? -1 : result;
    }
    
    @Override
    public BigInteger thresholdForLevel(int level) {
        String hql = "SELECT xp FROM " + clazz.getName() + " l WHERE l.level = :level";
        SelectionQuery<Long> query = getCurrentSession().createSelectionQuery(hql, Long.class).setParameter("level", level).setMaxResults(1);
        return BigInteger.valueOf(query.getSingleResult());
    }
    
    @Override
    public BigInteger nextThreshold(BigInteger current) {
        String hql = "SELECT xp FROM " + clazz.getName() + " l " + " WHERE l.xp > :xp " + " ORDER BY l.xp ASC";
        Long result = getCurrentSession().createSelectionQuery(hql, Long.class).setParameter("xp", current.longValue()).setMaxResults(1)
                .getSingleResult();
        return BigInteger.valueOf(result);
    }
    
    /**
     * @see bg.sarakt.storing.hibernate.interfaces.ILevelDAO#maximalThreshold()
     */
    @Override
    public BigInteger maximalThreshold() {
        String hql = "SELECT max(xp) FROM " + clazz.getName();
        
        SelectionQuery<Long> query = getCurrentSession().createSelectionQuery(hql, Long.class);
        Long result = query.getSingleResultOrNull();
        
        return result == null ? BigInteger.ZERO : BigInteger.valueOf(result.longValue());
    }
}
