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
public class SaraktException extends Exception
{

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202311230145L;

    public SaraktException()
    {
        super();
    }

    public SaraktException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SaraktException(String message)
    {
        super(message);
    }

    public SaraktException(Throwable cause)
    {
        super(cause);
    }

}



