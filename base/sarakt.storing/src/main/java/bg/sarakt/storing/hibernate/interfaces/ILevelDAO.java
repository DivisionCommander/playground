/*
 * ILevelDAO.java
 *
 * created at 2024-02-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.interfaces;

import java.math.BigInteger;
import java.util.NavigableMap;

import bg.sarakt.base.IHibernateDAO;
import bg.sarakt.storing.hibernate.entities.LevelEntity;

public interface ILevelDAO extends IHibernateDAO<LevelEntity> {
    
    int getMaxlevel();
    
    NavigableMap<Integer, LevelEntity> allAsMap();
    
    BigInteger thresholdForLevel(int level);
    
    BigInteger nextThreshold(BigInteger current);
    
    BigInteger maximalThreshold();
    
}
