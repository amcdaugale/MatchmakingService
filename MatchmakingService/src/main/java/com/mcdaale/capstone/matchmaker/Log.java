package com.mcdaale.capstone.matchmaker;

import java.util.IllegalFormatException;

/**
 * A class to handle logging for the Matchmaking service.
 * TODO: Add support to differentiate prod and dev environments, and add a method for productions logging.
 */
public class Log {
    /**
     * Application name.
     */
    private static final String APP = "MatchMakingService";

    /**
     * Enhance the message at the beginning of log with the method name and line number for debugging the application.
     * @param TAG The tag that denotes the class name.
     * @return Enhanced tag with added information for debugging.
     */
    private static String enhanceTag(String TAG) {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[2];
        return String.format("%s<%s.%s():%s>", APP, TAG, stackTraceElement.getMethodName(), stackTraceElement.getLineNumber());
    }

    /**
     * Print a debug log, includes extra information for debugging.
     * @param TAG The tag that denotes the class name.
     * @param body The Body of the log.
     */
    public static void d(String TAG, String body) {
        System.out.println(String.format("%s: %s", enhanceTag(TAG), body));
    }

    /**
     * Print a debug log, with a formatter string.
     * @param TAG The tag that denotes the class name.
     * @param body The Body of the log.
     * @param args The arguments to add to formatter string.
     */
    public static void d(String TAG, String body, Object... args) {

        try {
            Log.d(TAG, String.format(body, args));
        } catch (IllegalFormatException e) {
            Log.e(TAG, "%s: %s", e.getMessage(), "args and format string are not compatible.");
        }
    }

    /**
     * Print an error log.
     * @param TAG The tag that denotes the class name.
     * @param body The Body of the log.
     */
    public static void e(String TAG, String body) {
            System.err.println(String.format("%s: %s%n", enhanceTag(TAG), body));
    }

    /**
     * Print an error log with a formatter string.
     * @param TAG The tag that denotes the class name.
     * @param body The Body of the log.
     * @param args The arguments to add to formatter string.
     */
    public static void e(String TAG, String body, Object... args) {
        try {
            Log.e(TAG, String.format(body, args));
        } catch (IllegalFormatException e) {
            Log.e(TAG, "%s: %s", e.getMessage(), "args and format string are not compatible.");
        }
    }



}
