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

import bg.sarakt.base.IHibernateDAO;
import bg.sarakt.logging.Logger;
import bg.sarakt.storing.utils.HibernateUtils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractHibernateDAO<T extends Serializable> implements IHibernateDAO<T> {

    protected static final Logger LOGGER = Logger.getLogger();
    protected Class<T>            clazz;

    protected SessionFactory sessionFactory;

    
    /**
     * @see bg.sarakt.base.IHibernateDAO#isEntityClassVacant()
     */
    @Override
    public boolean isEntityClassVacant() { return this.clazz == null; }
    
    @Override
    public final void setEntityClass(Class<T> entityClass) { this.clazz = entityClass; }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }

    protected Session getCurrentSession() {
        if (sessionFactory != null) {
                try {
                    return sessionFactory.getCurrentSession();
                } catch (HibernateException e) {
                    return sessionFactory.openSession();
                }
        }
        LOGGER.error("Session factory for class=" + clazz + " is null!");
        return HibernateUtils.getSessionFactory().openSession();
    }

    @Override
    public T findOne(long id) {
        System.err.println(clazz);
        return getCurrentSession().get(clazz, id);
    }

    @Transactional
    @Override
    public List<T> findAll() {
        return getCurrentSession().createSelectionQuery("SELECT e FROM " + clazz.getName() + " e ", clazz).getResultList();
    }

    @Override
    public T save(T entity) {
        Session s = getCurrentSession();
        return s.merge(entity);
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AbstractHibernateDAO [clazz=" + this.clazz + ", sessionFactory=" + this.sessionFactory + "]";
    }
}
