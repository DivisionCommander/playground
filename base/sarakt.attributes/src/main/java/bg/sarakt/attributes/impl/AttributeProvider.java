/*
 * AttributeProvider.java
 *
 * created at 2024-02-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("defaultAttributeProvider")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AttributeProvider implements Attributes {
    
    private static final Logger LOG = Logger.getLogger();
    private Integer maxLevel;
    
    private NavigableMap<Integer, AttributeFormula> formulas;
    private NavigableMap<Integer, BigDecimal>       coefficients;
    
    private final Map<String, SecondaryAttribute> secondaryAttributes;
    private final Map<String, ResourceAttribute>  resourceAttributes;
    
    // @Autowired
    AttributeProvider(/*ILevelDAO dao,*/ @Value("${max.level.default.attributes:9}") int defaultMaxLevel) {
//        int dbMaxLevel = dao.getMaxlevel();
this.maxLevel = defaultMaxLevel;
//        LOG.debug("maxLevel " + maxLevel + "\tDBmax= " + dbMaxLevel + "\tdefaultMax= " + defaultMaxLevel);
        this.formulas = createDummyFormulas();
        this.coefficients = createDummyCoefficients();
        this.secondaryAttributes = createDefaultSecondaryAttributesMap();
        this.resourceAttributes = createDefaultResourceAttributesMap();
    }
    
    @Dummy(since = "0.0.10")
    private NavigableMap<Integer, AttributeFormula> createDummyFormulas() {
        NavigableMap<Integer, AttributeFormula> dummyFormulas = new TreeMap<>();
        for (int level = 1; level <= maxLevel; level++) {
            SimpleAttributeFormulaImpl formula = new SimpleAttributeFormulaImpl();
            formula.addAttributeFormula(PrimaryAttribute.STRENGTH, level);
            dummyFormulas.put(level, formula);
        }
        System.err.println("dummyFormulas" + dummyFormulas);
        return dummyFormulas;
    }
    
    @Dummy(since = "0.0.10")
    private NavigableMap<Integer, BigDecimal> createDummyCoefficients() {
        NavigableMap<Integer, BigDecimal> dummyCoefficients = new TreeMap<>();
        
        for (int level = 1; level <= maxLevel; level++) {
            BigDecimal coefficient = BigDecimal.ONE.multiply(new BigDecimal(level));
            dummyCoefficients.put(level, coefficient);
        }
        System.err.println("dummyCoefficients" + dummyCoefficients);
        return dummyCoefficients;
    }
    
    public Integer getMaxLevel() { return this.maxLevel; }
    
    public Map<String, SecondaryAttribute> defaultSecondaryAttributesMap() {
        return secondaryAttributes;
    }
    
    public Map<String, ResourceAttribute> defaultResourceAttributesMap() {
        return resourceAttributes;
    }
    
    private Map<String, SecondaryAttribute> createDefaultSecondaryAttributesMap() {
        Set<SecondaryAttributeImpl> set = new HashSet<>();
        
        // AttributeGroup.PHYSICAL:
        set.add(new SecondaryAttributeImpl(100, NAME_ACCURACY, ABBR_ACCURACY, AttributeGroup.PHYSICAL, DESC_ACCURACY));
        set.add(new SecondaryAttributeImpl(101, NAME_ATTACK_SKILL, ABBR_ATTACK_SKILL, AttributeGroup.PHYSICAL, DESC_ATTACK_SKILL));
        set.add(new SecondaryAttributeImpl(102, NAME_CRITICAL_CHANCE, ABBR_CRITICAL_CHANCE, AttributeGroup.PHYSICAL, DESC_CRITICAL_CHANCE));
        set.add(new SecondaryAttributeImpl(103, NAME_ATTACK_SPEED, ABBR_ATTACK_SPEED, AttributeGroup.PHYSICAL, DESC_ATTACK_SPEED));
        set.add(new SecondaryAttributeImpl(104, NAME_COMBAT_QUICKNESS, ABBR_COMBAT_QUICKNESS, AttributeGroup.PHYSICAL, DESC_COMBAT_QUICKNESS));
        set.add(new SecondaryAttributeImpl(105, NAME_HITTING, ABBR_HITTING, AttributeGroup.PHYSICAL, DESC_HITTING));
        set.add(new SecondaryAttributeImpl(106, NAME_DEFENCE_RATE, ABBR_DEFENCE_RATE, AttributeGroup.PHYSICAL, DESC_DEFENCE_RATE));
        set.add(new SecondaryAttributeImpl(109, NAME_ARMOR_PIERCING, ABBR_ARMOR_PIERCING, AttributeGroup.PHYSICAL, DESC_ARMOR_PIERCING));
        set.add(new SecondaryAttributeImpl(110, NAME_HIT_RATE, ABBR_HIT_RATE, AttributeGroup.PHYSICAL, DESC_HIT_RATE));
        set.add(new SecondaryAttributeImpl(111, NAME_EVADE, ABBR_EVADE, AttributeGroup.PHYSICAL, DESC_EVADE));
        
        // AttributeGroup.PSYCHICAL:
        set.add(new SecondaryAttributeImpl(200, NAME_IQ, ABBR_IQ, AttributeGroup.PSYCHICAL, DESC_IQ));
        set.add(new SecondaryAttributeImpl(201, NAME_CAST_RATE, ABBR_CAST_RATE, AttributeGroup.PSYCHICAL, DESC_CAST_RATE));
        set.add(new SecondaryAttributeImpl(204, NAME_RESISTANCE, ABBR_RESISTANCE, AttributeGroup.PSYCHICAL, DESC_RESISTANCE));
        set.add(new SecondaryAttributeImpl(205, NAME_KNOWLEDGE, ABBR_KNOWLEDGE, AttributeGroup.PSYCHICAL, DESC_KNOWLEDGE));
        
        // AttributeGroup.PERSON:
        set.add(new SecondaryAttributeImpl(301, NAME_COMBAT_RATING, ABBR_COMBAT_RATING, AttributeGroup.PERSON, DESC_COMBAT_RATING));
        set.add(new SecondaryAttributeImpl(302, NAME_CHAR, ABBR_CHAR, AttributeGroup.PERSON, DESC_CHAR));
        
        var map = set.stream().map(this::addFormula).collect(Collectors.toMap(SecondaryAttribute::fullName, sa -> sa));
        return Map.copyOf(map);
    }
    
    private Map<String, ResourceAttribute> createDefaultResourceAttributesMap() {
        Set<ResourceAttributeImpl> set = new HashSet<>();
        set.add(new ResourceAttributeImpl(107, NAME_LIFE, ABBR_LIFE, AttributeGroup.PHYSICAL, DESC_LIFE, PrimaryAttribute.CONSTITUTION));
        set.add(new ResourceAttributeImpl(202, NAME_MANA, ABBR_MANA, AttributeGroup.PSYCHICAL, DESC_MANA, PrimaryAttribute.INTELLIGENCE));
        set.add(new ResourceAttributeImpl(302, NAME_ENERGY, ABBR_ENERGY, AttributeGroup.PERSON, NAME_ENERGY, PrimaryAttribute.WILL));
        set.add(new ResourceAttributeImpl(303, NAME_VIGOUR, ABBR_VIGOUR, AttributeGroup.PERSON, NAME_VIGOUR, PrimaryAttribute.SPIRIT));
        var map = set.stream().map(this::addCoefficient).collect(Collectors.toMap(ResourceAttribute::fullName, ra -> ra));
        return Map.copyOf(map);
    }
    
    private SecondaryAttribute addFormula(SecondaryAttributeImpl attr) {
        attr.putFormulas(formulas);
        return attr;
    }
    
    private ResourceAttribute addCoefficient(ResourceAttributeImpl attr) {
        attr.addCoefficients(coefficients);
        return attr;
    }
}