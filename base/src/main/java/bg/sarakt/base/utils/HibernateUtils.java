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
import bg.sarakt.storing.hibernate.entities.LevelEntity;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeCoefficientEntity;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeEntity;
import bg.sarakt.storing.hibernate.entities.SecondaryAttributeEntity;
import bg.sarakt.storing.hibernate.entities.AttributeFormulaEntity;
import bg.sarakt.storing.hibernate.entities.UnitClassEntity;

public class HibernateUtils {

    public static SessionFactory getSessionFactory() {
        Configuration conf = new Configuration().configure()
        //@formatter:off
                .addAnnotatedClass(SecondaryAttributeEntity.class)
                .addAnnotatedClass(AttributeFormulaEntity.class)
                .addAnnotatedClass(ResourceAttributeEntity.class)
                .addAnnotatedClass(ResourceAttributeCoefficientEntity.class)
                .addAnnotatedClass(LevelEntity.class)
                .addAnnotatedClass(UnitClassEntity.class)
                .addAnnotatedClass(CreatureEntity.class)
                .addAnnotatedClass(TagEntity.class)
                ;
        //@formatter:on
        System.err.println("get as factory");
        return conf.buildSessionFactory();
    }

}
