/*
 * SaraktException.java
 *
 * created at 2023-11-23 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.base.exceptions;


/**
 * TODO short description for SaraktException.
 * <p>
 * Long description for SaraktException.
 *
 * @author IceDragon
 */
public class SaraktRuntimeException extends RuntimeException
{

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202311230147L;

    public SaraktRuntimeException()
    {
        super();
    }

    public SaraktRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SaraktRuntimeException(String message)
    {
        super(message);
    }

    public SaraktRuntimeException(Throwable cause)
    {
        super(cause);
    }

}



