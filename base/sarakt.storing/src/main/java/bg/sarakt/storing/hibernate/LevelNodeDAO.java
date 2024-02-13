/*
 * LevelNodeDAO.java
 *
 * created at 2024-01-27 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate;

import java.util.List;

import bg.sarakt.storing.hibernate.entities.LevelNodeEntity;
import bg.sarakt.storing.hibernate.interfaces.ILevelNodeDAO;

import org.hibernate.query.SelectionQuery;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LevelNodeDAO extends AbstractHibernateDAO<LevelNodeEntity> implements ILevelNodeDAO {
    
    /** field <code>CLASS_ID</code> */
    private static final String CLASS_ID = "classId";
    private String hqlSelect;
    
    public LevelNodeDAO() {
        super();
        setEntityClass(LevelNodeEntity.class);
        hqlSelect = hqlSelect();
    }
    
    private String hqlSelect() {
        return "SELECT n FROM " + clazz.getName() + " n JOIN FETCH n.additional WHERE ";
    }
    
    @Override
    public List<LevelNodeEntity> lookUpNodes(long experience, long classID) {
        String hql = hqlSelect + " n.unitClass.classId = :classId" + " AND n.levelEntity.xp > :xp ";
        SelectionQuery<LevelNodeEntity> query = getCurrentSession().createSelectionQuery(hql, clazz).setParameter(CLASS_ID, classID)
                .setParameter("xp", experience);
        return query.getResultList();
    }
    
    @Override
    public LevelNodeEntity lookUpNode(long experience, long classID) {
        String hql = hqlSelect + " n.unitClass.classId = :classId" + " AND n.levelEntity.xp <= :xp" + " ORDER BY n.levelEntity.xp DESC";
        SelectionQuery<LevelNodeEntity> query = getCurrentSession().createSelectionQuery(hql, clazz).setParameter(CLASS_ID, classID)
                .setParameter("xp", experience).setMaxResults(1);
        return query.getSingleResult();
    }
    
    @Override
    public LevelNodeEntity getClassNode(long classId, int level) {
        String hql = hqlSelect + " n.levelEntity.level = :level AND" + " n.unitClass.classId = :classID";
        SelectionQuery<LevelNodeEntity> query = getCurrentSession().createSelectionQuery(hql, clazz).setParameter("level", level)
                .setParameter("classID", classId);
        return query.getSingleResult();
    }
    
    @Override
    public List<LevelNodeEntity> getClassNodes(long classId) {
        String hql = hqlSelect + "n.unitClass.classId = :classId";
        SelectionQuery<LevelNodeEntity> query = getCurrentSession().createSelectionQuery(hql, clazz).setParameter(CLASS_ID, classId);
        
        return query.getResultList();
    }
    
    /**
     * @see bg.sarakt.storing.hibernate.interfaces.ILevelNodeDAO#getClassNodesFromLevel(long,
     *      int)
     */
    @Override
    public List<LevelNodeEntity> getClassNodesFromLevel(long classId, int level) {
        String hql = hqlSelect + " n.unitClass.classId = :classId " + " AND n.levelEntity.level >= :level";
        SelectionQuery<LevelNodeEntity> query = getCurrentSession().createSelectionQuery(hql, clazz).setParameter(CLASS_ID, classId)
                .setParameter("level", level);
        return query.getResultList();
    }
}
