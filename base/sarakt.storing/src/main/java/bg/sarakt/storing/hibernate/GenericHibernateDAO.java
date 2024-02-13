/*
 * GenericHibernateDAO.java
 *
 * created at 2024-01-31 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.storing.hibernate;

import java.io.Serializable;

import bg.sarakt.base.IHibernateDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("genericDao")
@Primary
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
public class GenericHibernateDAO<T extends Serializable> extends AbstractHibernateDAO<T> implements IHibernateDAO<T>{

    @Autowired
    public GenericHibernateDAO() {
        super();
    }
    
    public GenericHibernateDAO(Class<T> clz) {
        super();
        setEntityClass(clz);
    }
}



