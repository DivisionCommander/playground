/*
 * AttributeFactory.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.AttributeService;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.base.exceptions.UnknownValueException;
import bg.sarakt.base.utils.FormulaSerializer;
import bg.sarakt.logging.Logger;
import bg.sarakt.storing.hibernate.LevelDAO;
import bg.sarakt.storing.hibernate.entities.AttributeFormulaEntity;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeEntity;
import bg.sarakt.storing.hibernate.entities.SecondaryAttributeEntity;
import bg.sarakt.storing.hibernate.interfaces.IHibernateDAO;
import bg.sarakt.storing.hibernate.interfaces.ILevelDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
public final class AttributeFactory extends AttributeProvider implements Attributes, AttributeService {

    // @Autowired
    protected ILevelDAO lvl;
    
    private static AttributeFactory instance;
    
    @PostConstruct
    private void bindFactory() {
        instance = this; // NOSONAR
        LOG.debug("Binding AttributeFactory#factory to" + this);
    }
    
    private static final Logger LOG = Logger.getLogger();
    public static AttributeFactory getInstance() { return instance; }

    private final Map<String, SecondaryAttribute> secondaryAttributes;
    private final Map<String, ResourceAttribute>  resourceAttributes;


    @Autowired
    private AttributeFactory(IHibernateDAO<SecondaryAttributeEntity> secDao, IHibernateDAO<ResourceAttributeEntity> resDao) {
        Map<String, SecondaryAttribute> secondary = getSecondaryAttributesFromDB(secDao);
        if (secondary == null || secondary.isEmpty()) {
            secondary = defaultSecondaryAttributesMap();
        }
        Map<String, ResourceAttribute> resource = getResourceAttributesFromDB(resDao);
        if (resource == null || resource.isEmpty()) {
            resource = defaultResourceAttributesMap();
        }
        
        secondaryAttributes = Map.copyOf(secondary);
        resourceAttributes = Map.copyOf(resource);
    }

    @Override
    public  Collection< SecondaryAttribute> defaultSecondaryAttributes() {
        return defaultSecondaryAttributesMap().values();
    }
    @Override
    public Collection<ResourceAttribute> defaultResourceAttributes() {
        return defaultResourceAttributesMap().values();
    }
    @Autowired
    private Map<String, SecondaryAttribute> getSecondaryAttributesFromDB(IHibernateDAO<SecondaryAttributeEntity> dao) {
        try {
            if (dao.isEntityClassVacant()) {
                dao.setEntityClass(SecondaryAttributeEntity.class);
            }
            List<SecondaryAttributeEntity> results = dao.findAll();
            if (results == null || results.isEmpty()) {
                return Collections.emptyMap();
            }
            return results.stream().map(this::mapEntityToAttribute).collect(Collectors.toMap(Attribute::fullName, sa -> sa));
        } catch (Exception e) {
            LOG.error("Cannot get data from DB! Reason " + e.getMessage());
            return Collections.emptyMap();
        }
    }
    
    @Autowired
    private Map<String, ResourceAttribute> getResourceAttributesFromDB(IHibernateDAO<ResourceAttributeEntity> dao) {
        try {
            if (dao.isEntityClassVacant()) {
                dao.setEntityClass(ResourceAttributeEntity.class);
            }
            List<ResourceAttributeEntity> results = dao.findAll();
            if (results == null || results.isEmpty()) {
                return Collections.emptyMap();
            }
            return results.stream().map(this::mapEntityToAttribute).collect(Collectors.toMap(ResourceAttribute::fullName, ra -> ra));
        } catch (Exception e) {
            LOG.error("Cannot get data from DB! Reason " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    private SecondaryAttribute mapEntityToAttribute(SecondaryAttributeEntity e) {
        var sa = new SecondaryAttributeImpl(e.getId(), e.getName(), e.getAbbr(), e.getGroup(), e.getDescription());
        sa.putFormulas(convertEntityToFormulas(e.getFormulas()));
        return sa;
    }

    private ResourceAttribute mapEntityToAttribute(ResourceAttributeEntity e) {
        return new ResourceAttributeImpl(e.getId(), e.getName(), e.getAbbr(), e.getGroup(), e.getDescrption(), e.getPrimaryAttribute());
    }

    @Override
    public Collection<SecondaryAttribute> getSecondaryAttributes() { return secondaryAttributes.values(); }

    @Override
    public Collection<ResourceAttribute> getResourceAttribute() { return resourceAttributes.values(); }

    @Autowired
    void setLevelDao(@Qualifier("levelDAO") ILevelDAO jarjarbings) {
        System.out.println(jarjarbings);
        this.lvl = jarjarbings;
    }
    
    public int getMaxLevel() {
        
        return lvl.getMaxlevel();
        
    }

    /**
     * @param attribute
     * @return
     */
    @Override
    public Attribute ofName(String attribute) {
        try {
            return PrimaryAttribute.ofName(attribute);
        } catch (Exception e) {
            // IGNORE seams its not primary attribute
        }
        if (secondaryAttributes.containsKey(attribute)) {
            return secondaryAttributes.get(attribute);
        }
        if (resourceAttributes.containsKey(attribute)) {
            return resourceAttributes.get(attribute);
        }
        throw new UnknownValueException("Unknown or unsupported attribute!");
    }
    
    private NavigableMap<Integer, AttributeFormula> convertEntityToFormulas(List<AttributeFormulaEntity> formulas) {
        NavigableMap<Integer, AttributeFormula> results = new TreeMap<>();
        for (var formula : formulas) {
            try {
                byte[] bytearrayFormula = formula.getFormula();
                if (bytearrayFormula != null && bytearrayFormula.length > 0) {
                    AttributeFormula deseriallized = FormulaSerializer.getInstance().deseriallize(bytearrayFormula);
                    results.put(formula.getLevel(), deseriallized);
                    
                }
            } catch (Exception e) {
                LOG.error("FIXME:LOGG AND process error!");
            }
        }
        return results;
    }
}
