/*
 * IAdditionalAttrValuesDao.java
 *
 * created at 2024-02-04 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.interfaces;

import java.math.BigDecimal;

import bg.sarakt.base.IHibernateDAO;
import bg.sarakt.storing.hibernate.entities.AdditionalAttrValueEntity;

public interface IAdditionalAttrValuesDao extends IHibernateDAO<AdditionalAttrValueEntity> {
    
    
    AdditionalAttrValueEntity getOrSave(String attr, BigDecimal value);
    
}
