/*
 * DefaultResourceAttributes.java
 *
 * created at 2024-02-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.resources.impl;

import static bg.sarakt.attributes.utils.Attributes.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.internal.AbstractProvider;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.resources.ResourceAttribute;
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
        Set<ResourceAttributeImpl> set = new HashSet<>();
        set.add(new ResourceAttributeImpl(107, NAME_LIFE, ABBR_LIFE, AttributeGroup.PHYSICAL, DESC_LIFE, PrimaryAttribute.CONSTITUTION));
        set.add(new ResourceAttributeImpl(202, NAME_MANA, ABBR_MANA, AttributeGroup.PSYCHICAL, DESC_MANA, PrimaryAttribute.INTELLIGENCE));
        set.add(new ResourceAttributeImpl(302, NAME_ENERGY, ABBR_ENERGY, AttributeGroup.PERSON, NAME_ENERGY, PrimaryAttribute.WILL));
        set.add(new ResourceAttributeImpl(303, NAME_VIGOUR, ABBR_VIGOUR, AttributeGroup.PERSON, NAME_VIGOUR, PrimaryAttribute.SPIRIT));
        var map = set.stream().map(this::map).collect(Collectors.toMap(ResourceAttribute::fullName, ra -> ra));
        return Map.copyOf(map);
    }
    
    protected ResourceAttribute map(ResourceAttributeImpl attr) {
        attr.addCoefficients(coefficients);
        return attr;
    }
}
