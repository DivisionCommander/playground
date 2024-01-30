/*
 * Dummy.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.base.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mock-up, dummy or experimental class, using during development, testing or other activity.
 *
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(
    {
            ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR
    })
public @interface Dummy
{
    /**
     * Descriptions about the Dummy
     * @return
     */
    String description() default "";

    /**
     *
     * @since 0.0.6
     */
    String since() default "";

    /**
     *
     * @since 0.0.6
     */
    String to() default "";
}
