/*
 * ILevelDAO.java
 *
 * created at 2024-02-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.storing.hibernate.interfaces;

import bg.sarakt.storing.hibernate.entities.LevelEntity;

public interface ILevelDAO extends IHibernateDAO<LevelEntity> {
    
    int getMaxlevel();
    
}



