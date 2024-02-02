/*
 * AttributeProvider.java
 *
 * created at 2024-02-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.base.ApplicationContextProvider;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.characters.attributes1.impls.SimpleAttributeFormulaImpl;
import bg.sarakt.storing.hibernate.interfaces.ILevelDAO;

import org.springframework.context.ApplicationContext;

public class AttributeProvider implements Attributes{
    
    private int maxLevel;
    
    private static AttributeFormula getDummyFormula(int level) {
        
        SimpleAttributeFormulaImpl f = new SimpleAttributeFormulaImpl("Test");
        f.addAttributeFormula(PrimaryAttribute.STRENGTH, level);
        return f;
    }
    
    @Dummy
    private SortedMap<Integer, AttributeFormula> createDummyFormula() {
        NavigableMap<Integer, AttributeFormula> formulas = new TreeMap<>();
        for (int level = 1; level <= maxLevel; level++) {
            SimpleAttributeFormulaImpl formula = new SimpleAttributeFormulaImpl();
            formula.addAttributeFormula(PrimaryAttribute.STRENGTH, level);
            formulas.put(level, formula);
        }
        return formulas;
    }
    
    public int asd() {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        ILevelDAO bean = context.getBean(ILevelDAO.class);
        return bean.getMaxlevel();
    }
    
    protected Map<String, SecondaryAttribute> defaultSecondaryAttributesMap() {
        Map<String, SecondaryAttribute> map = new HashMap<>();
        
        // AttributeGroup.PHYSICAL:
        map.put(NAME_ACCURACY, new DummySecondaryAttribute(100, NAME_ACCURACY, "ACC", AttributeGroup.PHYSICAL, ""));
        map.put(NAME_ATTACK_SKILL, new DummySecondaryAttribute(101, NAME_ATTACK_SKILL, "ATT", AttributeGroup.PHYSICAL, ""));
        map.put(NAME_CRITICAL_CHANCE, new DummySecondaryAttribute(102, NAME_CRITICAL_CHANCE, "CC", AttributeGroup.PHYSICAL, ""));
        map.put(NAME_ATTACK_SPEED, new DummySecondaryAttribute(103, NAME_ATTACK_SPEED, "IAS", AttributeGroup.PHYSICAL, ""));
        map.put(NAME_COMBAT_QUICKNESS, new DummySecondaryAttribute(104, NAME_COMBAT_QUICKNESS, "CoQ", AttributeGroup.PHYSICAL, ""));
        map.put(NAME_HITTING, new DummySecondaryAttribute(105, NAME_HITTING, "HIT", AttributeGroup.PHYSICAL, ""));
        map.put(NAME_DEFENCE_RATE, new DummySecondaryAttribute(106, NAME_DEFENCE_RATE, "DEF", AttributeGroup.PHYSICAL, ""));
        map.put(NAME_ARMOR_PIERCING, new DummySecondaryAttribute(109, NAME_ARMOR_PIERCING, "ArP", AttributeGroup.PHYSICAL, ""));
        map.put(NAME_HIT_RATE, new DummySecondaryAttribute(110, NAME_HIT_RATE, "HRt", AttributeGroup.PHYSICAL, ""));
        map.put(NAME_EVADE, new DummySecondaryAttribute(111, NAME_EVADE, "EVA", AttributeGroup.PHYSICAL, ""));
        
        // AttributeGroup.PSYCHICAL:
        map.put(NAME_IQ, new DummySecondaryAttribute(200, NAME_IQ, "IQ", AttributeGroup.PSYCHICAL, ""));
        map.put(NAME_CAST_RATE, new DummySecondaryAttribute(201, NAME_CAST_RATE, "FCR", AttributeGroup.PSYCHICAL, ""));
        map.put(NAME_RESISTANCE, new DummySecondaryAttribute(204, NAME_RESISTANCE, "RES", AttributeGroup.PSYCHICAL, ""));
        map.put(NAME_KNOWLEDGE, new DummySecondaryAttribute(205, NAME_KNOWLEDGE, "KNW", AttributeGroup.PSYCHICAL, ""));
        
        // AttributeGroup.PERSON:
        map.put(NAME_COMBAT_RATING, new DummySecondaryAttribute(301, NAME_COMBAT_RATING, "CoR", AttributeGroup.PERSON, ""));
        return map;
    }

    protected Map<String, ResourceAttribute> defaultResourceAttributesMap() {
        Map<String, ResourceAttribute> map = new HashMap<>();
    
        map.put(NAME_HIT_POINTS, new DummyResourceAttribute(107, NAME_HIT_POINTS, "HP", PrimaryAttribute.CONSTITUTION, NAME_CURRENT_HIT_POINT));
        map.put(NAME_MANA_POINTS, new DummyResourceAttribute(202, NAME_MANA_POINTS, "MP", PrimaryAttribute.INTELLIGENCE, NAME_CURRENT_MANA_POINTS));
    
        map.put(NAME_ENERGY, new DummyResourceAttribute(302, NAME_ENERGY, "NGY", PrimaryAttribute.WILL, NAME_ENERGY));
        map.put(NAME_VIGOUR, new DummyResourceAttribute(303, NAME_VIGOUR, "VIG", PrimaryAttribute.SPIRIT, NAME_VIGOUR));
    
        return map;
    }

    public record DummyResourceAttribute(long getId, String fullName, String abbreviation, PrimaryAttribute getPrimaryAttribute, String description)
            implements ResourceAttribute {
        
        
        /**
         * @see bg.sarakt.attributes.Attribute#group()
         */
        @Override
        public AttributeGroup group() {
            return getPrimaryAttribute.group();
        }
        
        /**
         * @see bg.sarakt.attributes.ResourceAttribute#getCoefficientForLevel(bg.sarakt.attributes.levels.Level)
         */
        @Override
        public BigDecimal getCoefficientForLevel(Level level) {
            return BigDecimal.ONE.multiply(new BigDecimal(level.getLevelNumber()));
        }
        
        /**
         * @see bg.sarakt.attributes.ResourceAttribute#getEntry(bg.sarakt.attributes.AttributeMapEntry,
         *      bg.sarakt.attributes.levels.Level)
         * @deprecated
         */
        @Override
        @Deprecated(forRemoval = true)
        public ResourceAttributeEntry getEntry(AttributeMapEntry<PrimaryAttribute> primaryAttributeEntry, Level level) {
            return new ResourceAttributeEntry(this, primaryAttributeEntry, level);
        }
        
        @Override
        public ResourceAttributeEntry getEntry(AttributeMapEntry<PrimaryAttribute> primaryAttributeEntry) {
            return new ResourceAttributeEntry(this, primaryAttributeEntry);
        }
        
        @Override
        public String toString() {
            return fullName;
        }
        
    }
    
    public record DummySecondaryAttribute(long getId, String fullName, String abbreviation, AttributeGroup group, String description)
            implements SecondaryAttribute {
        
            
        /**
         * @see bg.sarakt.attributes.SecondaryAttribute#getFormula(int)
         */
        @Override
        public AttributeFormula getFormula(int level) {
            return getDummyFormula(level);
        }
        
        /**
         * @see bg.sarakt.attributes.SecondaryAttribute#getEntry(bg.sarakt.attributes.impl.PrimaryAttributeMap,
         *      bg.sarakt.attributes.levels.Level)
         */
        @Override
        public SecondaryAttributeEntry getEntry(IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primaryAttributes) {
            return new SecondaryAttributeEntry(this, primaryAttributes);
        }
        
        /**
         *
         * @see bg.sarakt.attributes.SecondaryAttribute#getEntry(bg.sarakt.attributes.IterableAttributeMap,
         *      bg.sarakt.attributes.levels.Level)
         * @deprecated
         */
        @Override
        @Deprecated(forRemoval = true)
        public SecondaryAttributeEntry getEntry(IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primaryAttributes, Level level) {
            return new SecondaryAttributeEntry(this, primaryAttributes, level);
        }
    }
    
}