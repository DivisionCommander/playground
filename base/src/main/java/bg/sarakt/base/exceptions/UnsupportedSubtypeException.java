/*
 * UnsupportedSubtypeException.java
 *
 * created at 2024-02-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.base.exceptions;

/**
 * 
 * When an subtype of (sealed) class or interface is used.
 */
public class UnsupportedSubtypeException extends IllegalArgumentException {
    
    private final Class<?>    unsupportedType;
    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202402020123L;
    
    public UnsupportedSubtypeException(Class<?> unsupported) {
        super();
        this.unsupportedType = unsupported;
    }
    
    public UnsupportedSubtypeException(Class<?> unsupported, String message, Throwable cause) {
        super(message, cause);
        this.unsupportedType = unsupported;
    }
    
    public UnsupportedSubtypeException(Class<?> unsupported, String s) {
        super(s);
        this.unsupportedType = unsupported;
    }
    
    public UnsupportedSubtypeException(Class<?> unsupported, Throwable cause) {
        super(cause);
        this.unsupportedType = unsupported;
    }

    public Class<?> getUnsupportedType() {
        return unsupportedType;
    }
    
    @Override
    public String toString() {
        return "UnsupportedSubtypeException [unsupportedType=" + unsupportedType + ", toString()=" + super.toString() + "]";
    }
    
}
