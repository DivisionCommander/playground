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

import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.base.utils.ForRemoval;
import bg.sarakt.storing.hibernate.entities.PrimaryAttributeValuesEntity;

public interface IPrimaryAttributeValuesDAO extends IHibernateDAO<PrimaryAttributeValuesEntity> {
    
    @Deprecated(since = "0.0.11", forRemoval = true)
    @ForRemoval(since = "0.0.11", expectedRemovalVersion = "0.0.15")
    PrimaryAttributeValuesEntity get(Map<PrimaryAttribute, BigInteger> map);
    
    PrimaryAttributeValuesEntity getOrSave(Map<PrimaryAttribute, BigInteger> map);
}
