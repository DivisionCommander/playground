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
import java.util.Objects;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

import bg.sarakt.base.utils.HibernateUtils;

@Repository
public abstract class AbstractHibernateDAO<T extends Serializable> {

    private Class<T> clazz;

    @Autowired
    protected SessionFactory sessionFactory;

    @Autowired
    protected LocalSessionFactoryBean sessionFactoryBean;


    protected AbstractHibernateDAO() {

    }

    public final void setClazz(final Class<T> clazzToSet) { clazz = Objects.requireNonNull(clazzToSet); }

    public void setSessionFactory(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }

    protected Session getCurrentSession() {
        System.out.println(sessionFactory + "\tsfb");
        System.out.println(sessionFactoryBean + "\tsfb");
        if( sessionFactory == null) {
            return HibernateUtils.getSessionFactory().openSession();
        }
        return sessionFactory.getCurrentSession();
    }

    public T findOne(long id) {
        return getCurrentSession().get(clazz, id);
    }

    public List<T> findAll() {
        return getCurrentSession().createSelectionQuery("SELECT t FROM Tags t", clazz).getResultList();
    }

}
