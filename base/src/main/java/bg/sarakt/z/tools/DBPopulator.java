/*
 * DBPopulator.java
 *
 * created at 2023-12-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.z.tools;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.attributes.impl.AttributeFactory;
import bg.sarakt.attributes.impl.Attributes;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.base.utils.FormulaSerializer;
import bg.sarakt.base.utils.HibernateUtils;
import bg.sarakt.logging.Logger;
import bg.sarakt.logging.LoggerFactory;
import bg.sarakt.storing.hibernate.AdditionaAttrValuesDAO;
import bg.sarakt.storing.hibernate.LevelDAO;
import bg.sarakt.storing.hibernate.PrimaryAttrValuesDAO;
import bg.sarakt.storing.hibernate.entities.AdditionalAttrValueEntity;
import bg.sarakt.storing.hibernate.entities.AttributeFormulaEntity;
import bg.sarakt.storing.hibernate.entities.LevelEntity;
import bg.sarakt.storing.hibernate.entities.LevelNodeEntity;
import bg.sarakt.storing.hibernate.entities.PrimaryAttributeValuesEntity;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeCoefficientEntity;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeEntity;
import bg.sarakt.storing.hibernate.entities.SecondaryAttributeEntity;
import bg.sarakt.storing.hibernate.entities.UnitClassEntity;

public final class DBPopulator {

    private static final Logger LOGGER = LoggerFactory.getLogger();

    private static final DBPopulator INSTANCE = new DBPopulator();

    private DBPopulator() {}

    @Autowired
    protected SessionFactory sessionFactory;

    public static DBPopulator getInstance() { return INSTANCE; }

    private synchronized Session beginSeesion() {
        if (sessionFactory != null) {
            return sessionFactory.getCurrentSession();

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

            if (session.isDirty()) {
                session.flush();
            }
            LOGGER.debug("Levels populated.");
        } catch (Exception e) {
            LOGGER.error("Level population failed! Reason=" + e.getMessage());
        }

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

            session.persist(e);
            session.persist(afe);

            if (session.isDirty())
                session.flush();
        } catch (Exception e) {
            LOGGER.error("Level population failed! Reason=" + e.getMessage());

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
                session.flush();
            }
        } catch (Exception e) {
            LOGGER.error("Attributes population failed! Reason=" + e.getMessage());
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

    private ResourceAttributeEntity map(ResourceAttribute attribute) {
        ResourceAttributeEntity e = new ResourceAttributeEntity();
        e.setId(attribute.getId());
        e.setName(attribute.fullName());
        e.setAbbr(attribute.abbreviation());
        e.setDescrption(attribute.description());
        e.setGroup(attribute.group());
        e.setPrimaryAttribute(attribute.getPrimaryAttribute());

        ResourceAttributeCoefficientEntity coef = new ResourceAttributeCoefficientEntity();
        coef.setResourceAttributeEntity(e);
        coef.setLevel(1);
        coef.setCoefficient(attribute.getCoefficientForLevel(Level.TEMP));
        e.setCoefficients(List.of(coef));

        return e;
    }

    public DBPopulator populateLevelNodes() {
        try (Session session = beginSeesion()) {
            PrimaryAttrValuesDAO pavDAO = new PrimaryAttrValuesDAO();
            AdditionaAttrValuesDAO aavDAO = new AdditionaAttrValuesDAO();
            LOGGER.debug("Populating level nodes.......");
            session.beginTransaction();

            UnitClassEntity uce1 = new UnitClassEntity();
            uce1.setClassName("drum class");

            UnitClassEntity uce = session.get(UnitClassEntity.class, 3L);
            if (uce == null) {
                uce = new UnitClassEntity();
                uce.setClassName("dummy class 2");
                uce = session.merge(uce);
                session.flush();
            }

            LevelDAO lvlDao = new LevelDAO();
            Map<Integer, LevelEntity> levels = lvlDao.findAll().stream().collect(Collectors.toMap(LevelEntity::getLevel, l -> l));

            var vaae0 = persistAddAttrValue(session, aavDAO, Attributes.NAME_ACCURACY, BigDecimal.TEN);
            var vaae1 = persistAddAttrValue(session, aavDAO, Attributes.NAME_ENERGY, BigDecimal.ONE);
            for (int level = 1; level < 16; level++) {
                Map<PrimaryAttribute, BigInteger> map = mapPrimaryAttribute(level);
                PrimaryAttributeValuesEntity primaryEntity = pavDAO.get(map);
                if (primaryEntity == null) {
                    primaryEntity = new PrimaryAttributeValuesEntity().fromIntegerMap(map);
                    primaryEntity = session.merge(primaryEntity);
                }
                var vaae2 = persistAddAttrValue(session, aavDAO, Attributes.NAME_HIT_POINTS, BigDecimal.valueOf(level));
                var vaae3 = persistAddAttrValue(session, aavDAO, Attributes.NAME_COMBAT_RATING, BigDecimal.valueOf(5 * level));
                var list = List.of(vaae0, vaae1, vaae2, vaae3);
                LevelNodeEntity node = new LevelNodeEntity(primaryEntity, list);
                LevelEntity lvl = levels.get(level);
                node.setLevelEntity(lvl);
                node.setUnitClass(uce);
                session.merge(node);
            }

            if (session.isDirty()) {
                session.flush();
            }
        } catch (Exception e) {
            LOGGER.error("Attributes population failed! Reason=" + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }


    private Map<PrimaryAttribute, BigInteger> mapPrimaryAttribute(int levelArg)
    {
        BigInteger level = BigInteger.valueOf(levelArg);
        Map<PrimaryAttribute, BigInteger> map = new EnumMap<>(PrimaryAttribute.class);
        map.put(PrimaryAttribute.STRENGTH, BigInteger.TWO);
        map.put(PrimaryAttribute.AGILITY, BigInteger.TWO);
        map.put(PrimaryAttribute.CONSTITUTION, BigInteger.TEN.multiply(level));

        map.put(PrimaryAttribute.INTELLIGENCE, level.divide(BigInteger.TWO));
        map.put(PrimaryAttribute.WISDOM, level.divide(BigInteger.TWO));
        map.put(PrimaryAttribute.PSIONIC, BigInteger.ONE.add(level.divide(BigInteger.TEN)));

        map.put(PrimaryAttribute.PSIONIC, level);
        map.put(PrimaryAttribute.WILL, BigInteger.TWO.add(level.divide(BigInteger.TEN)));


        return map;
    }

    private AdditionalAttrValueEntity persistAddAttrValue(Session s, AdditionaAttrValuesDAO dao, String attr, BigDecimal value) {
        AdditionalAttrValueEntity entity = dao.get(attr, value);
            if(entity == null) {
                entity =
        new AdditionalAttrValueEntity(attr, value);
                entity = s.merge(entity);
            }

        return entity;

    }
}
