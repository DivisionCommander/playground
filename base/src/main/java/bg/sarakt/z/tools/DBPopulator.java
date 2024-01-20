/*
 * DBPopulator.java
 *
 * created at 2023-12-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.z.tools;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.base.utils.FormulaSerializer;
import bg.sarakt.logging.Logger;
import bg.sarakt.logging.LoggerFactory;
import bg.sarakt.storing.hibernate.entities.AttributeFormulaEntity;
import bg.sarakt.storing.hibernate.entities.LevelEntity;
import bg.sarakt.storing.hibernate.entities.SecondaryAttributesEntity;

public class DBPopulator
{

    private final Logger LOGGER = LoggerFactory.getLogger();

    private static final DBPopulator INSTANCE = new DBPopulator();

    @Autowired
    protected SessionFactory sessionFactory;


    public static DBPopulator getInstance()
    {
        return INSTANCE;
    }

    private synchronized Session beginSeesion()
    {
        Session session = sessionFactory.getCurrentSession();
        return session;
    }

    public void populateLevels()
    {
        Session session = beginSeesion();

        try (session)
        {
            LOGGER.debug("Populating levels...");

            session.persist(new LevelEntity(Long.MIN_VALUE, 0));
            session.persist(new LevelEntity(0L, 1));
            session.persist(new LevelEntity(10L, 2));
            session.persist(new LevelEntity(20L, 3));
            session.persist(new LevelEntity(35L, 4));
            session.persist(new LevelEntity(50L, 5));
            session.persist(new LevelEntity(74L, 6));
            session.persist(new LevelEntity(100L, 7));
            session.persist(new LevelEntity(143L, 8));
            session.persist(new LevelEntity(180L, 9));
            session.persist(new LevelEntity(250L, 10));
            session.persist(new LevelEntity(350L, 11));
            session.persist(new LevelEntity(500L, 12));
            session.persist(new LevelEntity(700L, 13));
            session.persist(new LevelEntity(1000L, 14));
            session.persist(new LevelEntity(1800L, 15));

            System.out.println(session.isDirty());
            System.out.println(session.isDirty());
            session.flush();
        }
        LOGGER.debug("Levels populated.");
    }

    public void populateFormulas(SecondaryAttribute sa, int level)
    {
        try (Session session = beginSeesion())
        {
            SecondaryAttributesEntity e = new SecondaryAttributesEntity();
            e.setName(sa.fullName());
            e.setAbbr(sa.abbreviation());
            e.setDescription(sa.description());
            e.setGroup(sa.group());

            AttributeFormula formula = sa.getFormula(level);
            AttributeFormulaEntity afe = new AttributeFormulaEntity();
            afe.setAdvancedAttributesEntity(e);
            FormulaSerializer fes = FormulaSerializer.getInstance();
            afe.setAsString(fes.seriallizeToString(formula));
            afe.setFormula(fes.seriallize(formula));
            e.setFormulas(List.of(afe));

            System.out.println(afe);
            session.persist(e);
            session.persist(afe);

            session.flush();
        }
    }
}
