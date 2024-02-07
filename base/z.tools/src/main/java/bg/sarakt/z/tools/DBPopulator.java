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
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.AttributeService;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.attributes.impl.Attributes;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.utils.FormulaSerializer;
import bg.sarakt.base.IHibernateDAO;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.base.utils.ForRemoval;
import bg.sarakt.logging.Logger;
import bg.sarakt.logging.LoggerFactory;
import bg.sarakt.storing.hibernate.entities.AttributeFormulaEntity;
import bg.sarakt.storing.hibernate.entities.LevelEntity;
import bg.sarakt.storing.hibernate.entities.LevelNodeEntity;
import bg.sarakt.storing.hibernate.entities.PrimaryAttributeValuesEntity;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeCoefficientEntity;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeEntity;
import bg.sarakt.storing.hibernate.entities.SecondaryAttributeEntity;
import bg.sarakt.storing.hibernate.entities.UnitClassEntity;
import bg.sarakt.storing.hibernate.interfaces.IAdditionalAttrValuesDao;
import bg.sarakt.storing.hibernate.interfaces.ILevelDAO;
import bg.sarakt.storing.hibernate.interfaces.IPrimaryAttributeValuesDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@Profile("tools")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DBPopulator {

    private static final Logger LOGGER = LoggerFactory.getLogger();
    
    @Deprecated(since = "0.0.11", forRemoval = true)
    @ForRemoval(since = "0.0.11", expectedRemovalVersion = "0.0.15")
    private static DBPopulator instance;

    @Autowired
    protected DBPopulator(IPrimaryAttributeValuesDAO primAttrValDao, IAdditionalAttrValuesDao addAttrValDao, ILevelDAO levelDao,
            AttributeService attrService) {
        this.primAttrValDao = primAttrValDao;
        this.addAttrValDao = addAttrValDao;
        this.levelDAO = levelDao;
        this.attributeService = attrService;
        System.out.println("create");
    }

    private IPrimaryAttributeValuesDAO     primAttrValDao;
    private IAdditionalAttrValuesDao       addAttrValDao;
    private ILevelDAO                      levelDAO;
    @Autowired
    private IHibernateDAO<LevelNodeEntity> levelNodeDao;
    private AttributeService                        attributeService;
    @Autowired
    private IHibernateDAO<UnitClassEntity> classDAO;
    @Autowired
    private IHibernateDAO<SecondaryAttributeEntity> secDAO;
    @Autowired
    private IHibernateDAO<ResourceAttributeEntity>  resDAO;
    

    @Deprecated(since = "0.0.11", forRemoval = true)
    @ForRemoval(since = "0.0.11", expectedRemovalVersion = "0.0.15")
    public static DBPopulator getInstance() { return instance; }


    public DBPopulator populateLevels() {
        LOGGER.debug("Populating levels...");
        
        levelDAO.save((new LevelEntity(Long.MIN_VALUE, 0)));
        levelDAO.save((new LevelEntity(0L, 1)));
        levelDAO.save((new LevelEntity(10L, 2)));
        levelDAO.save((new LevelEntity(20L, 3)));
        levelDAO.save((new LevelEntity(35L, 4)));
        levelDAO.save((new LevelEntity(50L, 5)));
        levelDAO.save((new LevelEntity(74L, 6)));
        levelDAO.save((new LevelEntity(100L, 7)));
        levelDAO.save((new LevelEntity(143L, 8)));
        levelDAO.save((new LevelEntity(180L, 9)));
        levelDAO.save((new LevelEntity(250L, 10)));
        levelDAO.save((new LevelEntity(350L, 11)));
        levelDAO.save((new LevelEntity(500L, 12)));
        levelDAO.save((new LevelEntity(700L, 13)));
        levelDAO.save((new LevelEntity(1000L, 14)));
        levelDAO.save((new LevelEntity(1800L, 15)));
        
        LOGGER.debug("Levels populated.");
        return this;
    }

    public DBPopulator populateFormulas(SecondaryAttribute sa, int level) {
        try  {
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

            secDAO.save(e);

        } catch (Exception e) {
            LOGGER.error("Level population failed! Reason=" + e.getMessage());
            e.printStackTrace();

        }
        return this;
    }

    public DBPopulator populateAttributes() {
        
        try {
            LOGGER.debug("Begin populating secondary attributes");
            
            attributeService.defaultSecondaryAttributes().stream().map(this::map).forEach(secDAO::save);
            LOGGER.debug("Begin populating resource attributes");
            attributeService.defaultResourceAttributes().stream().map(this::map).forEach(resDAO::save);
            
            LOGGER.debug("Attriubtes populated");
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
        try  {
            LOGGER.debug("Populating level nodes.......");

            UnitClassEntity uce1 = new UnitClassEntity();
            uce1.setClassName("drum class");

            UnitClassEntity uce = classDAO.findOne(3L);
            if (uce == null) {
                uce = new UnitClassEntity();
                uce.setClassName("dummy class 2");
                uce = classDAO.save(uce);
            }

            Map<Integer, LevelEntity> levels = levelDAO.allAsMap();

            var vaae0 = addAttrValDao.getOrSave(Attributes.NAME_ACCURACY, BigDecimal.TEN);
            var vaae1 = addAttrValDao.getOrSave(Attributes.NAME_ENERGY, BigDecimal.ONE);
            
            int maxlevel = levelDAO.getMaxlevel();
            
            for (int level = 1; level < maxlevel; level++) {
                Map<PrimaryAttribute, BigInteger> map = mapPrimaryAttribute(level);
                PrimaryAttributeValuesEntity primaryEntity = primAttrValDao.getOrSave(map);
                var vaae2 = addAttrValDao.getOrSave(Attributes.NAME_LIFE, BigDecimal.valueOf(level));
                var vaae3 = addAttrValDao.getOrSave(Attributes.NAME_COMBAT_RATING, BigDecimal.valueOf(5L * level));
                var list = List.of(vaae0, vaae1, vaae2, vaae3);
                LevelNodeEntity node = new LevelNodeEntity(primaryEntity, list);
                LevelEntity lvl = levels.get(level);
                node.setLevelEntity(lvl);
                node.setUnitClass(uce);
                levelNodeDao.save(node);
                
            }

        } catch (Exception e) {
            LOGGER.error("Attributes population failed! Reason=" + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }

    @Dummy
    private Map<PrimaryAttribute, BigInteger> mapPrimaryAttribute(int levelArg) {
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

    
    /**
     * Helper method to bind this {@link Component} to
     * {@link DBPopulator#getInstance()}
     * 
     * @since 0.0.11
     * @deprecated would be removed along with other legacy fragments of manual
     *             singleton implementation
     */
    @Deprecated(since = "0.0.11", forRemoval = true)
    @ForRemoval(since = "0.0.11", expectedRemovalVersion = "0.0.15")
    @PostConstruct
    private void bindInstance() {
        instance = this;// NOSONAR
        LOGGER.debug("Binding DBPopulator#instance to\t" + this);
    }
    
}
