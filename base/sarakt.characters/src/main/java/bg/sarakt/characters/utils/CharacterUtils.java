/*
 * CharacterUtils.java
 *
 * created at 2024-02-16 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.characters.utils;

import java.math.BigDecimal;
import java.util.Collection;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.base.utils.ForRemoval;
import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.characters.attributes1.AttributeMap;

@SuppressWarnings("removal")
public final class CharacterUtils {
    
    /**
     * 
     * @param charMap
     * 
     * @return wrapped unmodified version of {@link CharacterAttributeMap} that
     *         supports only get actions. Any changes made to underlying
     *         {@link CharacterAttributeMap} affects the wrapped version.
     *         
     * @since 0.0.3 will be removed along with other instances of the old attribute
     *        system.
     * 
     */
    @ForRemoval(since = "0.0.3", expectedRemovalVersion = "0.0.5", description = "Temporary mapper that wrap the new implementation to pass as the old one")
    public static AttributeMap toOldAttributeMap(CharacterAttributeMap charMap) {
        return new AttributeMapProxy(charMap);
    }
    
    private static class AttributeMapProxy implements AttributeMap {
        
        private static final String UNSUPPORTED = "Unsupported";
        
        private final CharacterAttributeMap charMap;
        
        private AttributeMapProxy(CharacterAttributeMap charMap) {
            this.charMap = charMap;
        }
        
        /**
         * @see bg.sarakt.characters.attributes1.AttributeMap#setAttribute(bg.sarakt.characters.attributes.AttributeValuePair)
         */
        @Override
        public AttributeValuePair setAttribute(AttributeValuePair pair) {
            throw new UnsupportedOperationException(UNSUPPORTED);
        }
        
        /**
         * @see bg.sarakt.characters.attributes1.AttributeMap#setAttributes(java.util.Collection)
         */
        @Override
        public Collection<AttributeValuePair> setAttributes(Collection<AttributeValuePair> pairs) {
            throw new UnsupportedOperationException(UNSUPPORTED);
        }
        
        /**
         * @see bg.sarakt.characters.attributes1.AttributeMap#modifyAttribute(bg.sarakt.characters.attributes.AttributeValuePair)
         */
        @Override
        public void modifyAttribute(AttributeValuePair pair) {
            throw new UnsupportedOperationException(UNSUPPORTED);
        }
        
        /**
         * @see bg.sarakt.characters.attributes1.AttributeMap#modifyAttributes(java.util.Collection)
         */
        @Override
        public void modifyAttributes(Collection<AttributeValuePair> pairs) {
            throw new UnsupportedOperationException(UNSUPPORTED);
        }
        
        /**
         * @see bg.sarakt.characters.attributes1.AttributeMap#getAttribute(bg.sarakt.attributes.Attribute)
         */
        @Override
        public AttributeValuePair getAttribute(Attribute attr) {
            BigDecimal value = charMap.getCurrentAttributeValue(attr);
            return new AttributeValuePair(attr, value.intValue());
        }
        
        /**
         * @see bg.sarakt.characters.attributes1.AttributeMap#getAllAtributes()
         */
        @Override
        public Collection<AttributeValuePair> getAllAtributes() {
            return charMap.getAllValues().entrySet().stream().map(e -> new AttributeValuePair(e.getKey(), e.getValue().intValue())).toList();
        }
        
    }
    
    private CharacterUtils() {
        throw new UnsupportedOperationException("Cannot be instanced!");
    }
}



