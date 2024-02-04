/*
 * IAdditionalAttrValuesDao.java
 *
 * created at 2024-02-04 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.interfaces;

import java.math.BigDecimal;

import bg.sarakt.base.utils.ForRemoval;
import bg.sarakt.storing.hibernate.entities.AdditionalAttrValueEntity;

public interface IAdditionalAttrValuesDao extends IHibernateDAO<AdditionalAttrValueEntity> {
    
    @Deprecated(since = "0.0.11", forRemoval = true)
    @ForRemoval(since = "0.0.11", expectedRemovalVersion = "0.0.15")
    AdditionalAttrValueEntity get(String attr, BigDecimal value);
    
    AdditionalAttrValueEntity getOrSave(String attr, BigDecimal value);
    
}
