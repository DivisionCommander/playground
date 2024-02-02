/*
 * IHibernateDAO.java
 *
 * created at 2024-01-31 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.storing.hibernate.interfaces;

import java.io.Serializable;
import java.util.List;

public interface IHibernateDAO<T extends Serializable> {

    // T findOne(long id);

    List<T> findAll();

    void setEntityClass(Class<T> entityClass);

    boolean isEntityClassVacant();

}


