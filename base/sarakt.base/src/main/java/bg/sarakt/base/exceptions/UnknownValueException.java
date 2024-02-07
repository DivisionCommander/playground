/*
 * UnsupportedValueException.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.base.exceptions;


public class UnknownValueException extends SaraktRuntimeException
{

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 6983992815631481756L;

    public UnknownValueException()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public UnknownValueException(String message, Throwable cause)
    {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public UnknownValueException(String message)
    {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public UnknownValueException(Throwable cause)
    {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}



