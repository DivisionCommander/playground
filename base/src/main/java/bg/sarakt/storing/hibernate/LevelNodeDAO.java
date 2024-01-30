/*
 * LevelNodeDAO.java
 *
 * created at 2024-01-27 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.storing.hibernate;

import bg.sarakt.storing.hibernate.entities.LevelNodeEntity;

public class LevelNodeDAO  extends AbstractHibernateDAO<LevelNodeEntity>{

    public LevelNodeDAO() {
        super(LevelNodeEntity.class);
    }

}



