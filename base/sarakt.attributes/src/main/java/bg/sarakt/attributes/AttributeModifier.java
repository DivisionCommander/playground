/*
 * AttributeModifier.java
 *
 * created at 2024-01-14 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import java.math.BigDecimal;
import java.util.StringJoiner;

public interface AttributeModifier<A extends Attribute> {

    A getAttribute();

    BigDecimal getValue();

    ModifierType getBonusType();

    ModifierLayer getLayer();
    
    public static boolean isValid(AttributeModifier<?> modifier) {
        return checkForErrors(modifier).isBlank();
    }
    
    public static String checkForErrors(AttributeModifier<?> modifier) {
        
        if (modifier == null) {
            return "No modifier provided!";
        }
        StringJoiner validator = new StringJoiner(" ");
        if (modifier.getAttribute() == null) {
            validator.add("Missing attribute.");
        }
        if (modifier.getValue() == null) {
            validator.add("Missing value.");
        }
        if (modifier.getBonusType() == null) {
            validator.add("Missing bonus type");
        }
        if (modifier.getLayer() == null) {
            validator.add("Missing layer");
        }
        return validator.toString();
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    int hashCode();
    
    @Override
    boolean equals(Object obj);
    
    default boolean equals(AttributeModifier<?> modifier) {
        if ( !getAttribute().equals(modifier.getAttribute())) {
            return false;
        }
        if ( !getValue().equals(modifier.getValue())) {
            return false;
        }
        if ( !getBonusType().equals(modifier.getBonusType())) {
            return false;
        }
        return getLayer().equals(modifier.getLayer());
    }
}
