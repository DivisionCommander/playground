/*
 * AbstractProvider.java
 *
 * created at 2024-02-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.services.AttributeProviderService;
import bg.sarakt.logging.Logger;

public abstract class AbstractProvider<A extends Attribute> implements AttributeProviderService<A> {
    
    protected static final Logger LOG = Logger.getLogger();
    
    protected final Set<Class<? extends Attribute>> supportedClasses  = new HashSet<>();
    protected Map<String, A>                        defaultAttributes = new HashMap<>();
    
    private int maxLevel;
    
    protected AbstractProvider(int maxLevel) {
        this.maxLevel = maxLevel;
        supportedClasses.add(Attribute.class);
        init();
    }
    
    protected final void init() {
        refreshAttributes();
    }
    
    protected abstract void refreshMapping();
    
    protected abstract Map<String, A> generateDefault();
    
    protected final void registerClass(Class<? extends A> clz) {
        supportedClasses.add(clz);
    }
    
    @Override
    public final int getMaxLevel() { return this.maxLevel; }
    
    @Override
    public final void updateMaxLevel(int newMaxLevel) {
        this.maxLevel = newMaxLevel;
        init();
    }
    
    @Override
    public void refreshAttributes() {
        refreshMapping();
        this.defaultAttributes = generateDefault();
    }
    
    @Override
    public Optional<A> getAttribute(String name) {
        return Optional.ofNullable(defaultAttributes.get(name));
    }
    
    @Override
    public Map<String, A> getAttributeMap() { return defaultAttributes; }
    
    @Override
    public Collection<A> getAttributes() { return defaultAttributes.values(); }
    
    @Override
    public final Set<Class<? extends Attribute>> supportedClasses() {
        return Set.copyOf(supportedClasses);
    }
}
