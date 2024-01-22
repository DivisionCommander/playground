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
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.attributes.impl.AttributeFactory;
import bg.sarakt.base.utils.FormulaSerializer;
import bg.sarakt.base.utils.HibernateUtils;
import bg.sarakt.characters.Level;
import bg.sarakt.logging.Logger;
import bg.sarakt.logging.LoggerFactory;
import bg.sarakt.storing.hibernate.entities.LevelEntity;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeCoefficientEntity;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeEntity;
import bg.sarakt.storing.hibernate.entities.SecondaryAttributeEntity;
import bg.sarakt.storing.hibernate.entities.AttributeFormulaEntity;

public class DBPopulator {

    private final Logger LOGGER = LoggerFactory.getLogger();

    private static final DBPopulator INSTANCE = new DBPopulator();

    @Autowired
    protected SessionFactory sessionFactory;

    public static DBPopulator getInstance() { return INSTANCE; }

    private synchronized Session beginSeesion() {
        if (sessionFactory != null) {
            Session session = sessionFactory.getCurrentSession();
            return session;
        }
        return HibernateUtils.getSessionFactory().openSession();

    }

    public DBPopulator populateLevels() {
        Session session = beginSeesion();

        try (session) {
            LOGGER.debug("Populating levels...");

            session.beginTransaction();

            session.merge(new LevelEntity(Long.MIN_VALUE, 0));
            session.merge(new LevelEntity(0L, 1));
            session.merge(new LevelEntity(10L, 2));
            session.merge(new LevelEntity(20L, 3));
            session.merge(new LevelEntity(35L, 4));
            session.merge(new LevelEntity(50L, 5));
            session.merge(new LevelEntity(74L, 6));
            session.merge(new LevelEntity(100L, 7));
            session.merge(new LevelEntity(143L, 8));
            session.merge(new LevelEntity(180L, 9));
            session.merge(new LevelEntity(250L, 10));
            session.merge(new LevelEntity(350L, 11));
            session.merge(new LevelEntity(500L, 12));
            session.merge(new LevelEntity(700L, 13));
            session.merge(new LevelEntity(1000L, 14));
            session.merge(new LevelEntity(1800L, 15));

            System.out.println(session.isDirty());
            System.out.println(session.isDirty());
            session.flush();
        }
        LOGGER.debug("Levels populated.");

        return this;
    }

    public DBPopulator populateFormulas(SecondaryAttribute sa, int level) {
        try (Session session = beginSeesion()) {
            session.beginTransaction();
            SecondaryAttributeEntity e = new SecondaryAttributeEntity();
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

            if (session.isDirty())
                session.flush();
        } catch (Exception e) {
            System.out.println("ERROR!!!");
            e.printStackTrace();
        }
        return this;
    }

    public DBPopulator populateAttributes() {

        try (Session session = beginSeesion()) {
            session.beginTransaction();
            AttributeFactory factory = AttributeFactory.getInstance();
            for (SecondaryAttribute sa : factory.defaultSecondaryAttributes()) {
                SecondaryAttributeEntity e = map(sa);
                session.merge(e);
                e.getFormulas().forEach(session::merge);
            }

            factory.defaultResourceAttributes().stream().map(this::map).forEach(r ->
            {
                session.merge(r);
                r.getCoefficients().forEach(session::merge);
            }

            );
            if (session.isDirty()) {
                System.out.println("Flushing");
                session.flush();
            }
        }
        return this;
    }

    private SecondaryAttributeEntity map(SecondaryAttribute attribute) {
        SecondaryAttributeEntity e = new SecondaryAttributeEntity();
        e.setName(attribute.fullName());
        e.setAbbr(attribute.abbreviation());
        e.setDescription(attribute.description());
        e.setGroup(attribute.group());

        AttributeFormula formula = attribute.getFormula(1);
        AttributeFormulaEntity afe = new AttributeFormulaEntity();
        afe.setAdvancedAttributesEntity(e);
        FormulaSerializer fes = FormulaSerializer.getInstance();
        afe.setAsString(fes.seriallizeToString(formula));
        afe.setFormula(fes.seriallize(formula));
        e.setFormulas(List.of(afe));

        return e;
    }

    private ResourceAttributeEntity map(ResourceAttribute a) {
        ResourceAttributeEntity e = new ResourceAttributeEntity();
        e.setId(a.getId());
        e.setName(a.fullName());
        e.setAbbr(a.abbreviation());
        e.setDescrption(a.description());
        e.setGroup(a.group());
        e.setPrimaryAttribute(a.getPrimaryAttribute());

        ResourceAttributeCoefficientEntity coef = new ResourceAttributeCoefficientEntity();
        coef.setResourceAttributeEntity(e);
        coef.setLevel(1);
        coef.setCoefficient(a.getCoefficientForLevel(Level.TEMP));
        e.setCoefficients(List.of(coef));

        return e;
    }
}
