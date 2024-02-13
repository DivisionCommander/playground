/*
 * AttributeFactory.java
 *
 * created at 2024-01-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.impl.AttributeProvider;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.resources.ResourceAttribute;
import bg.sarakt.attributes.resources.ResourceAttributeBuilder;
import bg.sarakt.attributes.secondary.SecondaryAttribute;
import bg.sarakt.attributes.secondary.SecondaryAttributeBuilder;
import bg.sarakt.attributes.services.AttributeService;
import bg.sarakt.attributes.utils.FormulaSerializer;
import bg.sarakt.base.IHibernateDAO;
import bg.sarakt.base.exceptions.UnknownValueException;
import bg.sarakt.logging.Logger;
import bg.sarakt.storing.hibernate.entities.AttributeFormulaEntity;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeCoefficientEntity;
import bg.sarakt.storing.hibernate.entities.ResourceAttributeEntity;
import bg.sarakt.storing.hibernate.entities.SecondaryAttributeEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
public final class AttributeFactory implements AttributeService {
    
    private static AttributeFactory instance;
    
    private AttributeProvider provider;
    
    
    private static final Logger LOG = Logger.getLogger();
    
    public static AttributeFactory getInstance() { return instance; }
    
    private final Map<String, SecondaryAttribute> secondaryAttributes;
    private final Map<String, ResourceAttribute>  resourceAttributes;
    
    @Autowired
    private AttributeFactory(IHibernateDAO<SecondaryAttributeEntity> secDao, IHibernateDAO<ResourceAttributeEntity> resDao,
            // @Qualifier("defaultAttributeProvider") ComplexAttributeProviderService
            // attributeProvider,
            AttributeProvider attributeProvider,
            @Value("${load.default.attributes:false}") boolean loadDef) {
        Map<String, SecondaryAttribute> secondary = getSecondaryAttributesFromDB(secDao);
        Map<String, ResourceAttribute> resource = getResourceAttributesFromDB(resDao);
        secondaryAttributes = secondary == null ? new HashMap<>() : new HashMap<>(secondary);
        resourceAttributes = resource == null ? new HashMap<>() : new HashMap<>(resource);
        System.err.println(attributeProvider);
        this.provider = attributeProvider;
        if (loadDef) {
            resourceAttributes.putAll(provider.defaultResourceAttributes());
            secondaryAttributes.putAll(provider.defaultSecondaryAttributes());
        }
    }
    @Override
    public Collection<SecondaryAttribute> defaultSecondaryAttributes() {
        return provider.defaultSecondaryAttributes().values();
    }
    
    @Override
    public Collection<ResourceAttribute> defaultResourceAttributes() {
        return provider.defaultResourceAttributes().values();
    }
    
    @Override
    public Collection<SecondaryAttribute> getSecondaryAttributes() { return secondaryAttributes.values(); }


    @Override
    public Collection<ResourceAttribute> getResourceAttributes() { return resourceAttributes.values(); }


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

    @Override
    public void loadDefaultAttributes() {
        this.secondaryAttributes.clear();
        this.secondaryAttributes.putAll(provider.defaultSecondaryAttributes());
        this.resourceAttributes.clear();
        this.resourceAttributes.putAll(provider.defaultResourceAttributes());
    }

    private Map<String, SecondaryAttribute> getSecondaryAttributesFromDB(IHibernateDAO<SecondaryAttributeEntity> dao) {
        try {
            // if (dao.isEntityClassVacant()) {
            // System.out.println("force set entity");
            // dao.setEntityClass(SecondaryAttributeEntity.class);
            // }
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
    
    private Map<String, ResourceAttribute> getResourceAttributesFromDB(IHibernateDAO<ResourceAttributeEntity> dao) {
        try {
            if (dao.isEntityClassVacant()) {
                System.out.println("force set entity");
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
        SecondaryAttributeBuilder sab = new SecondaryAttributeBuilder();
        sab.setId(e.getId()).setAbbreviation(e.getAbbr()).setDescription(e.getDescription()).setGroup(e.getGroup());
        sab.addFormulas(convertEntityToFormulas(e.getFormulas()));
        return sab.build();
    }
    
    private ResourceAttribute mapEntityToAttribute(ResourceAttributeEntity e) {
        ResourceAttributeBuilder rab = new ResourceAttributeBuilder();
        rab.setId(e.getId()).setName(e.getName()).setAbbreviation(e.getAbbr()).setGroup(e.getGroup()).setDescription(e.getDescrption())
                .setPrimaryAttribute(e.getPrimaryAttribute());
        List<ResourceAttributeCoefficientEntity> coefficients = e.getCoefficients();
        coefficients.stream().forEach(c -> rab.addCoefficient(c.getLevel(), c.getCoefficient()));
        return rab.build();
        
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


    @PostConstruct
    private void bindFactory() {
        instance = this; // NOSONAR
        LOG.debug("Binding AttributeFactory#factory to" + this);
    }
}
