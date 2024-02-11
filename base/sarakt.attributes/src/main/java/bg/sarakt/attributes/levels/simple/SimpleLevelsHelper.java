/*
 * FlatLevelHelper.java
 *
 * created at 2024-02-10 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels.simple;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.impl.AttributeModifierRecord;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.primary.PrimaryAttributeProvider;
import bg.sarakt.attributes.resources.impl.DefaultResourceAttributesProvider;
import bg.sarakt.attributes.secondary.impl.DefaultSecondaryAttributesProvider;
import bg.sarakt.attributes.utils.Attributes;
import bg.sarakt.base.ApplicationContextProvider;

import org.springframework.context.ApplicationContext;

final class SimpleLevelsHelper implements Attributes {
    
    private final Set<String> resourceName   = Set.of(NAME_LIFE, NAME_MANA);
    private final Set<String> secondaryNames = Set.of(NAME_CHAR, NAME_ATTACK_SKILL, NAME_DEFENCE_RATE);
    private BigDecimal        primaryValue   = BigDecimal.TEN;
    private BigDecimal        resourceValue  = BigDecimal.TEN;
    private BigDecimal        seconaryValue  = BigDecimal.TWO;
    
    void injectModifiers(SimpleLevelNode node) {
        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        PrimaryAttributeProvider primary = ctx.getBean(PrimaryAttributeProvider.class);
        
        DefaultSecondaryAttributesProvider secondary = ctx.getBean(DefaultSecondaryAttributesProvider.class);
        DefaultResourceAttributesProvider resources = ctx.getBean(DefaultResourceAttributesProvider.class);
        Set<AttributeModifier<Attribute>> mods = new HashSet<>();
        primary.getAttributes().stream().map(this::mapPrimary).forEach(mods::add);
        for (String name : resourceName) {
            Optional<? extends Attribute> attr = resources.getAttribute(name);
            if (attr.isPresent()) {
                mods.add(new AttributeModifierRecord<>(attr.get(), resourceValue, ModifierType.COEFFICIENT, ModifierLayer.CLASS_LAYER));
            }
        }
        for (String name : secondaryNames) {
            Optional<? extends Attribute> opt = secondary.getAttribute(name);
            if (opt.isPresent()) {
                Attribute attr = opt.get();
                mods.add(new AttributeModifierRecord<>(attr, seconaryValue, ModifierType.FLAT_VALUE, ModifierLayer.CLASS_LAYER));
            }
        }
        node.injectModifiers(mods);
    }
    
    void setPrimaryValue(int value) { this.primaryValue = BigDecimal.valueOf(value); }
    
    void setResourceValue(double value) { this.resourceValue = BigDecimal.valueOf(value); }
    
    void setSecondaryValue(double value) { this.seconaryValue = BigDecimal.valueOf(value); }
    
    
    private AttributeModifier<Attribute> mapPrimary(PrimaryAttribute pa) {
        return new AttributeModifierRecord<>(pa, primaryValue, ModifierType.PRIMARY_PERMANENT, ModifierLayer.getLowestLayer());
    }
    
}