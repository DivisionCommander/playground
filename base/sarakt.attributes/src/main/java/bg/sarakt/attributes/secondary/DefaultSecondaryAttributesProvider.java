/*
 * DefaultSecondaryAttributesProvider.java
 *
 * created at 2024-02-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.secondary;

import static bg.sarakt.attributes.utils.Attributes.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.impl.SimpleAttributeFormulaImpl;
import bg.sarakt.attributes.internal.AbstractProvider;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.base.utils.Dummy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("defaultSecondaryProvider")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DefaultSecondaryAttributesProvider extends AbstractProvider<SecondaryAttribute> {
    
    private NavigableMap<Integer, AttributeFormula> formulas;
    
    public DefaultSecondaryAttributesProvider(@Value("${max.level.default.attributes:9}") int maxLevel) {
        super(maxLevel);
        this.refreshMapping();
        defaultAttributes = generateDefault();
        registerClass(SecondaryAttribute.class);
    }
    
    @Override
    @Dummy(since = "0.0.10")
    protected void refreshMapping() {
        NavigableMap<Integer, AttributeFormula> dummyFormulas = new TreeMap<>();
        for (int level = 1; level <= getMaxLevel(); level++) {
            SimpleAttributeFormulaImpl formula = new SimpleAttributeFormulaImpl();
            formula.addAttributeFormula(PrimaryAttribute.STRENGTH, level);
            dummyFormulas.put(level, formula);
        }
        LOG.debug("dummyFormulas" + dummyFormulas);
        formulas = dummyFormulas;
    }
    
    @Override
    protected Map<String, SecondaryAttribute> generateDefault() {
        Map<String, SecondaryAttribute> map = new HashMap<>();
        SecondaryAttributeBuilder sab = new SecondaryAttributeBuilder();
        
        // AttributeGroup.PHYSICAL:
        AttributeGroup group = AttributeGroup.PHYSICAL;
        sab.setId(100).setName(NAME_ACCURACY).setAbbreviation(ABBR_ACCURACY).setGroup(group).setDescription(DESC_ACCURACY).addFormulas(formulas);
        map.put(NAME_ACCURACY, sab.build());
        
        sab.setId(101).setName(NAME_ATTACK_SKILL).setAbbreviation(ABBR_ATTACK_SKILL).setGroup(group).setDescription(DESC_ATTACK_SKILL);
        sab.addFormulas(formulas);
        map.put(ABBR_ATTACK_SKILL, sab.build());
        
        sab.setId(102).setName(NAME_CRITICAL_CHANCE).setAbbreviation(ABBR_CRITICAL_CHANCE).setGroup(group).setDescription(DESC_CRITICAL_CHANCE);
        sab.addFormulas(formulas);
        map.put(NAME_CRITICAL_CHANCE, sab.build());
        
        sab.setId(103).setName(NAME_ATTACK_SPEED).setAbbreviation(ABBR_ATTACK_SPEED).setGroup(group).setDescription(DESC_ATTACK_SPEED);
        sab.addFormulas(formulas);
        map.put(NAME_ATTACK_SPEED, sab.build());
        
        sab.setId(104).setName(NAME_COMBAT_QUICKNESS).setAbbreviation(ABBR_COMBAT_QUICKNESS).setGroup(group).setDescription(DESC_COMBAT_QUICKNESS);
        sab.addFormulas(formulas);
        map.put(NAME_COMBAT_QUICKNESS, sab.build());
        
        sab.setId(105).setName(NAME_HITTING).setAbbreviation(ABBR_HITTING).setGroup(group).setDescription(DESC_HITTING).addFormulas(formulas);
        map.put(NAME_HITTING, sab.build());
        
        sab.setId(106).setName(NAME_DEFENCE_RATE).setAbbreviation(ABBR_DEFENCE_RATE).setGroup(group).setDescription(DESC_DEFENCE_RATE);
        sab.addFormulas(formulas);
        map.put(NAME_DEFENCE_RATE, sab.build());
        
        sab.setId(109).setName(NAME_ARMOR_PIERCING).setAbbreviation(ABBR_ARMOR_PIERCING).setGroup(group).setDescription(DESC_ARMOR_PIERCING);
        sab.addFormulas(formulas);
        map.put(NAME_ARMOR_PIERCING, sab.build());
        
        sab.setId(110).setName(NAME_HIT_RATE).setAbbreviation(ABBR_HIT_RATE).setGroup(group).setDescription(DESC_HIT_RATE).addFormulas(formulas);
        map.put(NAME_HIT_RATE, sab.build());
        
        sab.setId(111).setName(NAME_EVADE).setAbbreviation(ABBR_EVADE).setGroup(group).setDescription(DESC_EVADE).addFormulas(formulas);
        map.put(NAME_EVADE, sab.build());
        
        // AttributeGroup.PSYCHICAL:
        group = AttributeGroup.PSYCHICAL;
        sab.setId(200).setName(NAME_IQ).setAbbreviation(ABBR_IQ).setGroup(group).setDescription(DESC_IQ).addFormulas(formulas);
        map.put(NAME_IQ, sab.build());
        
        sab.setId(201).setName(NAME_CAST_RATE).setAbbreviation(ABBR_CAST_RATE).setGroup(group).setDescription(DESC_CAST_RATE).addFormulas(formulas);
        map.put(NAME_CAST_RATE, sab.build());
        
        sab.setId(204).setName(NAME_RESISTANCE).setAbbreviation(ABBR_RESISTANCE).setGroup(group).setDescription(DESC_RESISTANCE);
        sab.addFormulas(formulas);
        map.put(NAME_RESISTANCE, sab.build());
        
        sab.setId(205).setName(NAME_KNOWLEDGE).setAbbreviation(ABBR_KNOWLEDGE).setGroup(group).setDescription(DESC_KNOWLEDGE).addFormulas(formulas);
        map.put(NAME_KNOWLEDGE, sab.build());
        
        // AttributeGroup.PERSON:
        group = AttributeGroup.PERSON;
        sab.setId(301).setName(NAME_COMBAT_RATING).setAbbreviation(ABBR_COMBAT_RATING).setGroup(group).setDescription(DESC_COMBAT_RATING);
        sab.addFormulas(formulas);
        map.put(NAME_COMBAT_RATING, sab.build());
        
        sab.setId(302).setName(NAME_CHAR).setAbbreviation(ABBR_CHAR).setGroup(group).setDescription(DESC_CHAR).addFormulas(formulas);
        map.put(NAME_CHAR, sab.build());
        
        return Map.copyOf(map);
    }
    
}
