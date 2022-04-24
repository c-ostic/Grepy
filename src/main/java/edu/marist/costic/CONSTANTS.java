package edu.marist.costic;

/**
 * App utility class for constants.
 */
public final class CONSTANTS {
    /** Application name. */
    private static String appName = "grepyCO";
    /** Application version. */
    private static String version = "0.0.1";
    /** Default NFA dot file name */
    private static String nfaDot = "nfa.dot";
    /** Default DFA dot file name */
    private static String dfaDot = "dfa.dot";

    /**
     * CONSTANTS constructor.
     */
    private CONSTANTS() {
    }

    /**
     * App version getter.
     * {@link CONSTANTS#version}
     * @return Version string of app
     */
    public static String getVersion() {
        return version;
    }

    /**
     * App name getter.
     * @return App name string
     */
    public static String getAppName() {
        return appName;
    }

    /**
     * Default NFA file name getter
     * @return Default NFA file name
     */
    public static String getDefaultNFAFile() {
        return nfaDot;
    }

    /**
     * Default DFA file name getter
     * @return Default DFA file name
     */
    public static String getDefaultDFAFile() {
        return dfaDot;
    }
}
