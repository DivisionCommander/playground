/*
 * LevelNodeDAO.java
 *
 * created at 2024-01-27 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.storing.hibernate;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import bg.sarakt.storing.hibernate.entities.LevelNodeEntity;

@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Deprecated(forRemoval = true, since = "0.0.8")
public class LevelNodeDAO  extends AbstractHibernateDAO<LevelNodeEntity>{

    public LevelNodeDAO() {
        super();
        setEntityClass(LevelNodeEntity.class);
    }
}



