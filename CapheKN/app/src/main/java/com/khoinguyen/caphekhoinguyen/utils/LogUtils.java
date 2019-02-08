package com.khoinguyen.caphekhoinguyen.utils;

import android.util.Log;

/**
 * Custom logging class to use instead of the standard android.util.Log.
 *
 * @author Dung Ho <br>.
 */

public final class LogUtils {

    private LogUtils() {
    }

    /**
     * Custom log levels
     */
    public enum LogLevel {
        /**
         * The log level such that no logging is shown.
         */
        LOG_LEVEL_NONE,
        /**
         * The log level such that only Error messages are shown.
         */
        LOG_LEVEL_ERROR,
        /**
         * The log level such that only Warning and Error messages are shown.
         */
        LOG_LEVEL_WARNING,
        /**
         * The log level such that only Info, Warning and Error messages are shown.
         */
        LOG_LEVEL_INFO,
        /**
         * The log level such that only Debug, Info, Warning and Error messages are shown.
         */
        LOG_LEVEL_DEBUG,
        /**
         * The log level such that only Verbose, Debug, Info, Warning and Error messages are shown.
         */
        LOG_LEVEL_VERBOSE
    }

    private static LogLevel m_nLogLevel = LogLevel.LOG_LEVEL_VERBOSE;

    /**
     * Sets the log level as a value from LogLevel enum.
     * Default value for the log level is LOG_LEVEL_WARNING.
     * Besides this method, log level is set by VbConfSet TraceLevel property.
     *
     * @param level the logging level
     */
    public static void setLogLevel(LogLevel level) {
        m_nLogLevel = level;
    }

    static void setLogLevel(String level) {
        if (level.equalsIgnoreCase("NONE")) m_nLogLevel = LogLevel.LOG_LEVEL_NONE;
        else if (level.equalsIgnoreCase("ERROR")) m_nLogLevel = LogLevel.LOG_LEVEL_ERROR;
        else if (level.equalsIgnoreCase("WARNING")) m_nLogLevel = LogLevel.LOG_LEVEL_WARNING;
        else if (level.equalsIgnoreCase("INFO")) m_nLogLevel = LogLevel.LOG_LEVEL_INFO;
        else if (level.equalsIgnoreCase("DEBUG")) m_nLogLevel = LogLevel.LOG_LEVEL_DEBUG;
        else if (level.equalsIgnoreCase("VERBOSE")) m_nLogLevel = LogLevel.LOG_LEVEL_VERBOSE;
        else m_nLogLevel = LogLevel.LOG_LEVEL_WARNING;
    }

    /**
     * Stores a string in the system logcat stream if the class LogLevel is LOG_LEVEL_DEBUG or lower.
     *
     * @param logTag the message tag: typically, the capitalized class name saved in a class static variable.
     * @param szText the message text.
     */
    public static void d(String logTag, String szText) {
        WriteLog(LogLevel.LOG_LEVEL_DEBUG, logTag, szText);
    }

    /**
     * Stores a string in the system logcat stream if the class LogLevel is LOG_LEVEL_INFO or lower.
     *
     * @param logTag the message tag: typically, the capitalized class name saved in a class static variable.
     * @param szText the message text.
     */
    public static void i(String logTag, String szText) {
        WriteLog(LogLevel.LOG_LEVEL_INFO, logTag, szText);
    }

    /**
     * Stores a string in the system logcat stream if the class LogLevel is LOG_LEVEL_WARNING or lower.
     *
     * @param logTag the message tag: typically, the capitalized class name saved in a class static variable.
     * @param szText the message text.
     */
    public static void w(String logTag, String szText) {
        WriteLog(LogLevel.LOG_LEVEL_WARNING, logTag, szText);
    }

    /**
     * Stores a string in the system logcat stream if the class LogLevel is LOG_LEVEL_VERBOSE or lower.
     *
     * @param logTag the message tag: typically, the capitalized class name saved in a class static variable.
     * @param szText the message text.
     */
    public static void v(String logTag, String szText) {
        WriteLog(LogLevel.LOG_LEVEL_VERBOSE, logTag, szText);
    }

    /**
     * Stores a string in the system logcat stream if the class LogLevel is LOG_LEVEL_ERROR or lower.
     *
     * @param logTag the message tag: typically, the capitalized class name saved in a class static variable.
     * @param szText the message text.
     */
    public static void e(String logTag, String szText) {
        WriteLog(LogLevel.LOG_LEVEL_ERROR, logTag, szText);
    }

    /**
     * Using when entered a function or beginning a block of code
     *
     * @param logTag the message tag: typically, the capitalized class name saved in a class static variable.
     * @param szText the message text.
     */
    public static void enter(String logTag, String szText) {
        WriteLog(LogLevel.LOG_LEVEL_DEBUG, logTag, szText + " - IN");
    }

    /**
     * Using before going out a function or block of code
     *
     * @param logTag the message tag: typically, the capitalized class name saved in a class static variable.
     * @param szText the message text.
     */
    public static void exit(String logTag, String szText) {
        WriteLog(LogLevel.LOG_LEVEL_DEBUG, logTag, szText + " - OUT");
    }

    /*
     * method used to write something that can be captured with logcat
     */
    private static void WriteLog(LogLevel nLevel, String logTag, String szText) {
        //if (m_nLogLevel != LogLevel.LOG_LEVEL_NONE)
        if (m_nLogLevel != LogLevel.LOG_LEVEL_NONE && nLevel.ordinal() <= m_nLogLevel.ordinal()) {
            switch (nLevel) {
                case LOG_LEVEL_DEBUG:
                    Log.d(logTag, szText);
                    break;
                case LOG_LEVEL_INFO:
                    Log.i(logTag, szText);
                    break;
                case LOG_LEVEL_WARNING:
                    Log.w(logTag, szText);
                    break;
                case LOG_LEVEL_ERROR:
                    Log.e(logTag, szText);
                    break;
                case LOG_LEVEL_VERBOSE:
                    Log.v(logTag, szText);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Writes useful info on the system logcat when an exception is caught.
     *
     * @param oException the exception to log
     */
    public static void writeException(Throwable oException) {
        if (oException == null)
            throw new IllegalArgumentException("Exception object cannot be null");
        final String tag = oException.getClass().getSimpleName();
        WriteLog(LogLevel.LOG_LEVEL_ERROR, tag, "***** An exception (" + oException.getClass().getName() + ") has been thrown.");
        if (oException.getMessage() != null) {
            WriteLog(LogLevel.LOG_LEVEL_ERROR, tag, oException.getMessage());
        }
        StackTraceElement[] aoStackTrace = oException.getStackTrace();
        for (int nIdx = 0; nIdx < aoStackTrace.length; nIdx++) {
            WriteLog(LogLevel.LOG_LEVEL_DEBUG, tag, aoStackTrace[nIdx].toString());
        }
        WriteLog(LogLevel.LOG_LEVEL_ERROR, tag, "*************** " + oException.getClass().getName() + " ***************");
    }

}
