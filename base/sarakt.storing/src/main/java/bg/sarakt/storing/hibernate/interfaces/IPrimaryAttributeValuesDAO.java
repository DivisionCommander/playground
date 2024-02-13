/*
 * IPrimaryAttributeValuesDAO.java
 *
 * created at 2024-02-04 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.interfaces;

import java.math.BigInteger;
import java.util.Map;

import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.base.IHibernateDAO;
import bg.sarakt.storing.hibernate.entities.PrimaryAttributeValuesEntity;

public interface IPrimaryAttributeValuesDAO extends IHibernateDAO<PrimaryAttributeValuesEntity> {
    
    PrimaryAttributeValuesEntity getOrSave(Map<PrimaryAttribute, BigInteger> map);
}
