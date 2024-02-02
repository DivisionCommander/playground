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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;

import bg.sarakt.base.utils.HibernateUtils;
import bg.sarakt.logging.Logger;
import bg.sarakt.storing.hibernate.interfaces.IHibernateDAO;

@Transactional
public abstract class AbstractHibernateDAO<T extends Serializable> implements IHibernateDAO<T> {

    protected static final Logger LOGGER = Logger.getLogger();
    protected Class<T>            clazz;

    @Autowired
    protected LocalSessionFactoryBean sessionFactory;

    // @Autowired
    // protected SessionFactory sf;

    @Autowired
    protected TransactionManager transactionManager;
    
    /**
     * @see bg.sarakt.storing.hibernate.interfaces.IHibernateDAO#isEntityClassVacant()
     */
    @Override
    public boolean isEntityClassVacant() { return this.clazz == null; }
    
    @Override
    public final void setEntityClass(Class<T> entityClass) { this.clazz = entityClass; }

    public void setSessionFactory(LocalSessionFactoryBean sessionFactory) { this.sessionFactory = sessionFactory; }

    protected Session getCurrentSession() {
        if (sessionFactory != null) {
            SessionFactory sf = sessionFactory.getObject();
            if (sf != null) {
                try {
                    return sf.getCurrentSession();
                } catch (HibernateException e) {
                    return sf.openSession();
                }
            }
        }
        System.err.println("sf is null");
        return HibernateUtils.getSessionFactory().openSession();
    }

    // @Override
    public T findOne(long id) {
        return getCurrentSession().get(clazz, id);
    }

    @Transactional
    @Override
    public List<T> findAll() {
        return getCurrentSession().createSelectionQuery("SELECT e FROM " + clazz.getName() + " e ", clazz).getResultList();
    }

}
