/*
 * InventoryException.java
 *
 * created at 2024-01-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.base.exceptions;

public class InventoryException extends SaraktException {

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202401081557L;

    /**
     *
     */
    public InventoryException(String message) {
        super(message);
    }

    public InventoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
