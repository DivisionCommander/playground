/*
 * Logger.java created at 2019-03-21 by r.tsonev <YOURMAILADDRESS> Copyright (c)
 * SEEBURGER AG, Germany. All Rights Reserved.
 */

package bg.sarakt.logging;

public interface Logger
{

    public static Logger getLogger()
    {
        return LoggerFactory.getLogger();
    }

    void log(Object object);

    void error(Object object);

    void debug(Object object);

    void combat(Object o);

    public enum LogLevels
    {
        CONSOLE_ONLY,
        FILE_ONLY,
        BOTH
    }

}
