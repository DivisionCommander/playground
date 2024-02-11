/*
 * PrimaryAttributeProvider.java
 *
 * created at 2024-02-10 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.primary;

import static bg.sarakt.attributes.utils.Attributes.*;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.services.AttributeProviderService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("defaultPrimaryProvider")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public final class PrimaryAttributeProvider implements AttributeProviderService<PrimaryAttribute> {
    
    private int                                 maxLevel;
    private final Map<String, PrimaryAttribute> attributes;
    
    public PrimaryAttributeProvider(@Value("${max.level.default.attributes:9}") int maxLevel) {
        var map = PrimaryAttribute.getAllPrimaryAttributes().stream().collect(Collectors.toMap(PrimaryAttribute::fullName, pa -> pa));
        attributes = Map.copyOf(map);
        this.maxLevel = maxLevel;
    }
    
    /**
     * @see bg.sarakt.attributes.services.AttributeProviderService#getMaxLevel()
     */
    @Override
    public int getMaxLevel() { return maxLevel; }
    
    /**
     * @see bg.sarakt.attributes.services.AttributeProviderService#updateMaxLevel(int)
     */
    @Override
    public void updateMaxLevel(int newMaxLevel) {
        this.maxLevel = newMaxLevel;
        // No other affects
    }
    
    /**
     * @see bg.sarakt.attributes.internal.AbstractProvider#getAttribute(java.lang.String)
     */
    @Override
    public Optional<PrimaryAttribute> getAttribute(String name) {
        try {
            return Optional.of(PrimaryAttribute.valueOf(name));
        } catch (IllegalArgumentException ex) {
            // skip - its not the constant name. Processing with the actual attribute data
        }
        
        PrimaryAttribute pa = switch (name)
        {
        case NAME_STRENGTH, ABBR_STRENGTH -> PrimaryAttribute.STRENGTH;
        case NAME_AGILITY, ABBR_AGILITY -> PrimaryAttribute.AGILITY;
        case NAME_CONSTITUTION, ABBR_CONSTITUTION -> PrimaryAttribute.CONSTITUTION;
    
        case NAME_INTELLIGENCE, ABBR_INTELLIGENCE -> PrimaryAttribute.INTELLIGENCE;
        case NAME_WISDOM, ABBR_WISDOM -> PrimaryAttribute.WISDOM;
        case NAME_PSIONIC, ABBR_PSIONIC -> PrimaryAttribute.PSIONIC;
    
        case NAME_SPIRIT, ABBR_SPIRIT -> PrimaryAttribute.SPIRIT;
        case NAME_WILL, ABBR_WILL -> PrimaryAttribute.WILL;
        case NAME_XP, ABBR_XP -> PrimaryAttribute.EXPERIENCE;
    
        default -> null;
        };
        return Optional.ofNullable(pa);
    }
    
    /**
     * @see bg.sarakt.attributes.internal.AbstractProvider#refreshAttributes()
     */
    @Override
    public void refreshAttributes() {
        /**
         * NO-OP: Since underlying Attribute type is an enum, there could not be any
         * changes
         **/
    }
    
    /**
     * @see bg.sarakt.attributes.services.AttributeProviderService#getAttributes()
     */
    @Override
    public Map<String, PrimaryAttribute> getAttributeMap() { return attributes; }
    
    /**
     * @see bg.sarakt.attributes.services.AttributeProviderService#getAttributes()
     */
    @Override
    public Collection<PrimaryAttribute> getAttributes() { return attributes.values(); }
    
    /**
     * @see bg.sarakt.attributes.services.AttributeProviderService#supportedClasses()
     */
    @Override
    public Set<Class<? extends Attribute>> supportedClasses() {
        return Set.of(Attribute.class, PrimaryAttribute.class);
    }
}
