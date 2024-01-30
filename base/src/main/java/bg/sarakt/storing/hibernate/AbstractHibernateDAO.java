/*
 * AbstractHibernateDAO.java
 *
 * created at 2023-12-06 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

import bg.sarakt.base.utils.HibernateUtils;
import bg.sarakt.logging.Logger;

@Repository
public abstract class AbstractHibernateDAO<T extends Serializable> {

    protected static final Logger LOGGER = Logger.getLogger();
    protected Class<T>            clazz;

    @Autowired
    protected SessionFactory sessionFactory;

    @Autowired
    protected LocalSessionFactoryBean sessionFactoryBean;


    protected AbstractHibernateDAO(Class<T> entityClass) {
        this.clazz = entityClass;
    }

    public void setSessionFactory(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }

    protected Session getCurrentSession() {
        LOGGER.debug(sessionFactory + "\t s fact");
        LOGGER.debug(sessionFactoryBean + "\t f bean");
        if (sessionFactory == null) {
            // TODO: better interaction with Spring and @Autowired and uncomment following
            // code
            return HibernateUtils.getSessionFactory().openSession();
        }
        return sessionFactory.getCurrentSession();
    }

    public T findOne(long id) {
        return getCurrentSession().get(clazz, id);
    }

    public List<T> findAll() {
        return getCurrentSession().createSelectionQuery("SELECT e FROM " + clazz.getName() + " e ", clazz).getResultList();
    }

}
