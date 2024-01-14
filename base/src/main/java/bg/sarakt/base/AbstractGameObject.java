/*
 * AbstractGameObject.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.base;

import java.util.Objects;

public abstract class AbstractGameObject implements GameObject {

    /** field <code>serialVersionUID</code> */
    private static final long   serialVersionUID          = 1L;
    /** field <code>representationSignatureID</code> */
    private static final String representationSignatureID = "2023-nov-28-abstract-GO";

    private String signatureId;
    private long   instanceId;

    protected AbstractGameObject() {
        this(representationSignatureID, System.currentTimeMillis());
    }

    protected AbstractGameObject(String representationSignatureID) {
        this(representationSignatureID, System.currentTimeMillis());
    }

    protected AbstractGameObject(String representationSignatureID, long instanceId) {
        this.signatureId = representationSignatureID;
        this.instanceId = instanceId;
    }

    /**
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameObject go) {
            return equals(go);
        }
        return Objects.deepEquals(this, obj);
    }

    /**
     * @see bg.sarakt.base.GameObject#equals(bg.sarakt.base.GameObject)
     */
    @Override
    public boolean equals(GameObject obj) {
        return (this.id().equals(obj.id()) && this.instanceId == obj.instanceId());
    }

    /**
     * @see bg.sarakt.base.GameObject#id()
     */
    @Override
    public String id() {
        return signatureId;
    }

    /**
     * @see bg.sarakt.base.GameObject#instanceId()
     */
    @Override
    public long instanceId() {
        return instanceId;
    }
}
