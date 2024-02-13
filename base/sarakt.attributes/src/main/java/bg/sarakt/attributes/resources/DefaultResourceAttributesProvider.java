/*
 * DefaultResourceAttributes.java
 *
 * created at 2024-02-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.resources;

import static bg.sarakt.attributes.utils.Attributes.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.internal.AbstractProvider;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.base.utils.Dummy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("defaultResourceProvider")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DefaultResourceAttributesProvider extends AbstractProvider<ResourceAttribute> {
    
    private NavigableMap<Integer, BigDecimal> coefficients;
    
    public DefaultResourceAttributesProvider(@Value("${max.level.default.attributes:9}") int maxLevel) {
        super(maxLevel);
        this.refreshMapping();
        defaultAttributes = generateDefault();
        registerClass(ResourceAttribute.class);
    }
    
    @Override
    @Dummy(since = "0.0.10")
    protected void refreshMapping() {
        NavigableMap<Integer, BigDecimal> dummyCoefficients = new TreeMap<>();
        for (int level = 1; level <= getMaxLevel(); level++) {
            BigDecimal coefficient = BigDecimal.ONE.multiply(new BigDecimal(level));
            dummyCoefficients.put(level, coefficient);
        }
        LOG.debug("dummyCoefficients" + dummyCoefficients);
        this.coefficients = dummyCoefficients;
    }
    
    @Override
    protected Map<String, ResourceAttribute> generateDefault() {
        Map<String, ResourceAttribute> map = new HashMap<>();
        ResourceAttributeBuilder rab = new ResourceAttributeBuilder();
        
        rab.setId(107).setName(NAME_LIFE).setAbbreviation(ABBR_LIFE).setGroup(AttributeGroup.PHYSICAL).setDescription(DESC_LIFE);
        rab.setPrimaryAttribute(PrimaryAttribute.CONSTITUTION).addCoefficients(coefficients);
        map.put(NAME_LIFE, rab.build());
        
        rab.setId(202).setName(NAME_MANA).setAbbreviation(ABBR_MANA).setGroup(AttributeGroup.PSYCHICAL).setDescription(DESC_MANA);
        rab.setPrimaryAttribute(PrimaryAttribute.INTELLIGENCE).addCoefficients(coefficients);
        map.put(NAME_MANA, rab.build());
        
        rab.setId(302).setName(NAME_ENERGY).setAbbreviation(ABBR_ENERGY).setGroup(AttributeGroup.PERSON).setDescription(NAME_ENERGY);
        rab.setPrimaryAttribute(PrimaryAttribute.WILL).addCoefficients(coefficients);
        map.put(NAME_ENERGY, rab.build());
        
        rab.setId(303).setName(NAME_VIGOUR).setAbbreviation(ABBR_VIGOUR).setGroup(AttributeGroup.PERSON).setDescription(NAME_VIGOUR);
        rab.setPrimaryAttribute(PrimaryAttribute.SPIRIT).addCoefficients(coefficients);
        map.put(NAME_VIGOUR, rab.build());
        
        return Map.copyOf(map);
    }
}
