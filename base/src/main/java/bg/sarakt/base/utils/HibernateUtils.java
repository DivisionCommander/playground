/*
 * HibernateUtils.java
 *
 * created at 2023-12-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.base.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import bg.sarakt.glossary.entitites.CreatureEntity;
import bg.sarakt.glossary.entitites.TagEntity;
import bg.sarakt.storing.hibernate.entities.AttributeFormulaEntity;
import bg.sarakt.storing.hibernate.entities.LevelEntity;
import bg.sarakt.storing.hibernate.entities.SecondaryAttributesEntity;
import bg.sarakt.storing.hibernate.entities.UnitClassEntity;

public class HibernateUtils {

    public static SessionFactory getSessionFactory() {
        Configuration conf = new Configuration().configure()
        //@formatter:off
                .addAnnotatedClass(SecondaryAttributesEntity.class)
                .addAnnotatedClass(AttributeFormulaEntity.class)
                .addAnnotatedClass(LevelEntity.class)
                .addAnnotatedClass(UnitClassEntity.class)
                .addAnnotatedClass(CreatureEntity.class)
                .addAnnotatedClass(TagEntity.class);
        //@formatter:on
        System.err.println("get as factory");
        return conf.buildSessionFactory();
    }

}
