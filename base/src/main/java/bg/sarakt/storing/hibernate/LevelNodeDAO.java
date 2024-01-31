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
public class LevelNodeDAO  extends AbstractHibernateDAO<LevelNodeEntity>{

    public LevelNodeDAO() {
        super();
        setClazz(LevelNodeEntity.class);
    }
}



