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
import bg.sarakt.logging.Logger;
import bg.sarakt.storing.hibernate.entities.AttributeFormulaEntity;
import bg.sarakt.storing.hibernate.entities.LevelEntity;
import bg.sarakt.storing.hibernate.entities.LevelNodeEntity;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeCoefficientEntity;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeEntity;
import bg.sarakt.storing.hibernate.entities.SecondaryAttributeEntity;
import bg.sarakt.storing.hibernate.entities.UnitClassEntity;
import bg.sarakt.storing.hibernate.entities.AdditionalAttrValueEntity;
import bg.sarakt.storing.hibernate.entities.PrimaryAttributeValuesEntity;

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
                .addAnnotatedClass(LevelNodeEntity.class)
                .addAnnotatedClass(AdditionalAttrValueEntity.class)
                .addAnnotatedClass(PrimaryAttributeValuesEntity.class)
                ;
        //@formatter:on
        System.err.println("get as factory");
        Logger.getLogger().error( "!!!!!");
        return conf.buildSessionFactory();
    }

}
