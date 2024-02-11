/*
 * AttributeProvider.java
 *
 * created at 2024-02-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.primary.PrimaryAttributeProvider;
import bg.sarakt.attributes.resources.ResourceAttribute;
import bg.sarakt.attributes.secondary.SecondaryAttribute;
import bg.sarakt.attributes.services.AttributeProviderService;
import bg.sarakt.base.exceptions.UnsupportedSubtypeException;
import bg.sarakt.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("defaultAttributeProvider")
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Primary
public class AttributeProvider implements AttributeProviderService<Attribute> {
    
    private static final Logger LOG = Logger.getLogger();
    private Integer             maxLevel;
    
    private final Set<Class<? extends Attribute>> supportedClasses = Set.of(Attribute.class, PrimaryAttribute.class, ResourceAttribute.class,
            SecondaryAttribute.class);
    
    private AttributeProviderService<PrimaryAttribute>   primaryProvider;
    private AttributeProviderService<ResourceAttribute>  resourceProvider;
    private AttributeProviderService<SecondaryAttribute> secondaryProvider;
    
    @Autowired
    public AttributeProvider(@Value("${max.level.default.attributes:9}") int defaultMaxLevel, PrimaryAttributeProvider defaultPrimary,
            AttributeProviderService<ResourceAttribute> defaultResourceProvider,
            AttributeProviderService<SecondaryAttribute> defaultSecondaryProvider) {
        this.maxLevel = defaultMaxLevel;
        this.primaryProvider = defaultPrimary;
        this.resourceProvider = defaultResourceProvider;
        this.secondaryProvider = defaultSecondaryProvider;
    }
    
    @Override
    public int getMaxLevel() { return this.maxLevel; }
    
    @Override
    public void updateMaxLevel(int newMaxLevel) {
        if (this.maxLevel.intValue() == newMaxLevel) {
            LOG.error("The same value for <Max Level> is already set.");
            return;
        }
        LOG.debug("Updating Max level from <" + this.maxLevel + "> to the new value <" + newMaxLevel + ">.");
        this.maxLevel = newMaxLevel;
        refreshAttributes();
        
    }
    
    @Override
    public void refreshAttributes() {
        resourceProvider.refreshAttributes();
        secondaryProvider.refreshAttributes();
    }
    
    @Override
    public Optional<Attribute> getAttribute(String name) {
        Optional<? extends Attribute> optional = primaryProvider.getAttribute(name);
        if (optional.isEmpty()) {
            optional = resourceProvider.getAttribute(name);
        }
        if (optional.isEmpty()) {
            optional = secondaryProvider.getAttribute(name);
        }
        return optional.isEmpty() ? Optional.empty() : Optional.of(optional.get());
    }
    
    // TODO: better attribute retrieveing
    public <C extends Attribute> Optional<C> getAttribute(String name, Class<C> clazz) {
        if ( !supportedClasses.contains(clazz)) {
            throw new UnsupportedSubtypeException(clazz);
        }
        
        // For revision: better handling of attribute retrieving.
        Optional<Attribute> attribute = getAttribute(name);
        if (attribute.isPresent() && clazz.isInstance(attribute)) {
            C value = clazz.cast(attribute);
            return Optional.of(value);
        }
        
        return Optional.empty();
    }
    
    @Override
    public Map<String, Attribute> getAttributeMap() {
        Map<String, Attribute> attributes = new HashMap<>();
        attributes.putAll(primaryProvider.getAttributeMap());
        attributes.putAll(resourceProvider.getAttributeMap());
        attributes.putAll(secondaryProvider.getAttributeMap());
        
        return attributes;
    }
    
    /**
     * @see bg.sarakt.attributes.services.AttributeProviderService#getAttributes()
     */
    @Override
    public Collection<Attribute> getAttributes() { return getAttributeMap().values(); }
    
    public Map<String, SecondaryAttribute> defaultSecondaryAttributes() {
        return secondaryProvider.getAttributeMap();
    }
    
    public Map<String, ResourceAttribute> defaultResourceAttributes() {
        return resourceProvider.getAttributeMap();
    }
    
    @Override
    public Set<Class<? extends Attribute>> supportedClasses() {
        return supportedClasses;
    }
    
}