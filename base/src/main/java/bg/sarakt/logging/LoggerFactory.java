/*
 * LoggerFactory.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.time.LocalTime;

import bg.sarakt.logging.Logger.LogLevels;

public final class LoggerFactory
{

    private static final Logger LOGGER = new LoggerImpl(new File(prepareFolder(), "Log-" + System.currentTimeMillis() + ".log"),
            LogLevels.CONSOLE_ONLY);

    private static String prepareFolder()
    {
        File file = Paths.get("src" + File.separator + "LOGS").toFile();
        file.mkdirs();
        return file.getAbsolutePath();
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    private LoggerFactory()
    {
        /** NOOP **/
    }

    static class LoggerImpl implements Logger
    {

        /** field <code>INFO</code> */
        public static final String INFO   = "INFO";   // NOSONAR
        /** field <code>DEBUG</code> */
        public static final String DEBUG  = "DEBUG";  // NOSONAR
        /** field <code>ERROR</code> */
        public static final String ERROR  = "ERROR";  // NOSONAR
        /** field <code>COMBAT</code> */
        public static final String COMBAT = "COMBAT"; // NOSONAR

        private final PrintStream writer;
        private final LogLevels   level;

        public LoggerImpl(String location, LogLevels log)
        {
            this(new File(location), log);
        }

        public LoggerImpl(File loggingLocation, LogLevels logLevel)
        {
            PrintStream bw;
            try
            {
                bw = new PrintStream(new FileOutputStream(loggingLocation));
            }
            catch (IOException e)
            {
                bw = null;
            }
            writer = bw;
            level = logLevel;
        }

        @Override
        public void log(Object o)
        {
            String toPrint = formatObject(o, INFO);
            if (level == LogLevels.CONSOLE_ONLY || level == LogLevels.BOTH)
            {
                System.out.println(toPrint); // NOSONAR
            }
            if (writer != null && (level == LogLevels.FILE_ONLY || level == LogLevels.BOTH))
            {
                writer.println(toPrint);
            }
        }

        @Override
        public void combat(Object o)
        {
            String toPrint = formatObject(o, COMBAT);
            if (level == LogLevels.CONSOLE_ONLY || level == LogLevels.BOTH)
            {
                System.out.println(toPrint); // NOSONAR
            }
            if (writer != null && (level == LogLevels.FILE_ONLY || level == LogLevels.BOTH))
            {
                writer.println(toPrint);
            }
        }

        @Override
        public void error(Object o)
        {
            String toPrint = formatObject(o, ERROR);
            if (level == LogLevels.CONSOLE_ONLY || level == LogLevels.BOTH)
            {
                System.err.println(toPrint); // NOSONAR
            }
            if (writer != null && (level == LogLevels.FILE_ONLY || level == LogLevels.BOTH))
            {
                writer.println(toPrint);
            }
        }

        @Override
        public void debug(Object o)
        {
            String toPrint = formatObject(o, DEBUG);
            System.err.println(toPrint); // NOSONAR
            if (writer != null)
            {
                writer.println(toPrint);
            }

        }

        private String formatObject(Object o, String level)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(LocalTime.now()).append("] - [").append(level).append("]\t");
            sb.append(o.toString());

            return sb.toString();
        }
    }

}
