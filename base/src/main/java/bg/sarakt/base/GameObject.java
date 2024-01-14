/*
 * GameObject.java
 *
 * created at 2023-11-23 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.base;

import java.io.Serializable;

/**
 * Primary interface for a game object that have physical appearance not only
 * value.
 *
 * @author IceDragon
 */
public interface GameObject extends Comparable<GameObject>, Serializable
{

    /**
     * ID of the object by definition. The value is same for all instances.
     *
     * @return
     */
    String id();

    /**
     * The unique identifier of current instance.
     *
     * @return
     */
    long instanceId();

    /**
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    boolean equals(GameObject gameObject);

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    default int compareTo(GameObject char2)
    {
        return Long.compare(instanceId(), char2.instanceId());
    }
}
