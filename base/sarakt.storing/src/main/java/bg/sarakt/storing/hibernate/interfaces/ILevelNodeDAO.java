/*
 * ILevelNodeDAO.java
 *
 * created at 2024-02-12 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.interfaces;

import java.util.List;

import bg.sarakt.base.IHibernateDAO;
import bg.sarakt.storing.hibernate.entities.LevelNodeEntity;

public interface ILevelNodeDAO extends IHibernateDAO<LevelNodeEntity> {
    
    LevelNodeEntity lookUpNode(long experience, long classID);
    
    List<LevelNodeEntity> lookUpNodes(long experience, long classID);
    
    LevelNodeEntity getClassNode(long classId, int level);
    
    List<LevelNodeEntity> getClassNodes(long classId);
    
    List<LevelNodeEntity> getClassNodesFromLevel(long classId, int level);
}
