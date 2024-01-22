/*
 * LevelDAO.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate;

import bg.sarakt.storing.hibernate.entities.LevelEntity;

public class LevelDAO extends AbstractHibernateDAO<LevelEntity> {

    public LevelDAO() {
        super(LevelEntity.class);
    }
}
