/*
 * ForRemoval.java
 *
 * created at 2024-02-04 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.base.utils;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To be used in pair with {@link Deprecated} since in {@link Deprecated} there
 * is no param to specify when the annotated element will be removed.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value =
    {
            CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, MODULE, PARAMETER, TYPE
})
public @interface ForRemoval
{
    
    String UNKNOWN_VERSION = "UNKNOWN";
    
    /**
     * Returns the version in which the annotated element became deprecated.
     *
     * @return the version string
     * 
     * @see Deprecated#since()
     */
    String since() default UNKNOWN_VERSION;
    
    /**
     * Returns the version in which the annotated element is expected to be removed;
     *
     * @return the version string
     * @since 9
     */
    String expectedRemovalVersion();
    
    /**
     * Reason to be removed at first place. Usually, duplicate the deprecated tag in
     * javadoc. Since this is personal project and I often skip documenting code,
     * this is a convenient way to keep notes.
     */
    String description() default "";
}
