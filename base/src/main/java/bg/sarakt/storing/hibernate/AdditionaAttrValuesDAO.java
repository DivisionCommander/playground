/*
 * AdditionaAttrValuesDAO.java
 *
 * created at 2024-01-29 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate;

import java.math.BigDecimal;

import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import bg.sarakt.base.utils.ForRemoval;
import bg.sarakt.storing.hibernate.entities.AdditionalAttrValueEntity;
import bg.sarakt.storing.hibernate.interfaces.IAdditionalAttrValuesDao;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AdditionaAttrValuesDAO extends AbstractHibernateDAO<AdditionalAttrValueEntity> implements IAdditionalAttrValuesDao {
    
    public AdditionaAttrValuesDAO() {
        super();
        setEntityClass(AdditionalAttrValueEntity.class);
    }
    
    @Deprecated(since = "0.0.11", forRemoval = true)
    @ForRemoval(since = "0.0.11", expectedRemovalVersion = "0.0.15")
    @Override
    public AdditionalAttrValueEntity get(String attr, BigDecimal value) {
        String hql = "FROM AdditionalAttributeValues" + " WHERE attribute=:attr" + " AND value=:value";
        
        Session session = getCurrentSession();
        SelectionQuery<AdditionalAttrValueEntity> query = session.createSelectionQuery(hql, clazz);
        query.setParameter("attr", attr);
        query.setParameter("value", value);
        
        return query.getSingleResultOrNull();
    }
    
    @Override
    public AdditionalAttrValueEntity getOrSave(String attr, BigDecimal value) {
        String hql = "FROM AdditionalAttributeValues" + " WHERE attribute=:attr" + " AND value=:value";
        
        Session session = getCurrentSession();
        SelectionQuery<AdditionalAttrValueEntity> query = session.createSelectionQuery(hql, clazz);
        query.setParameter("attr", attr);
        query.setParameter("value", value);
        
        AdditionalAttrValueEntity result = query.getSingleResultOrNull();
        if (result == null) {
            return session.merge(new AdditionalAttrValueEntity(attr, value));
        }
        return result;
    }
    
}
