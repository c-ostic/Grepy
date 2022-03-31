package edu.marist.gildein;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * General utilty and helper methods.
 */
public class Utils {
    /**
     * Print help text.
     *
     * @param options CLI options to print
     */
    public void printHelpText(final Options options) {
        System.out.println(CONSTANTS.getAppName());
        System.out.println("Version: " + CONSTANTS.getVersion());

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(CONSTANTS.getAppName(), options);
    }

    /**
     * Initialize CLI options.
     *
     * @return Options of CLI for use in parser
     */
    public Options initOptions() {
        Options options = new Options();
        options.addOption("v", false, "Verbose mode");
        options.addOption("h", false, "Display this help text");
        return options;
    }

    /**
     * Process command line arguments using Apache Commons CLI.
     *
     * @param args Arguments from command line
     * @return Parsed command line output
     */
    public CommandLine processArgs(final String[] args) {
        Options options = initOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException pe) {
            printHelpText(options);
            error("Error parsing command line: " + pe.getMessage());
        }

        if (cmd.hasOption("h")) {
            printHelpText(options);
            System.exit(0);
        } else if (cmd.hasOption("v")) {
            System.out.println("Verbose mode");
            System.out.println(CONSTANTS.getAppName()
                    + " version: "
                    + CONSTANTS.getVersion());
        }

        return cmd;
    }

    /**
     * Error helper method to print error message to stderr
     * and exit with return code.
     *
     * @param msg        Error message to be printed
     * @param returnCode Return code to exit with
     */
    public void error(final String msg, final int returnCode) {
        System.err.println(msg);
        System.exit(returnCode);
    }

    /**
     * Error helper method to print error message to stderr
     * and exit with return code -1.
     *
     * @param msg Error message to be printed
     */
    public void error(final String msg) {
        System.err.println(msg);
        System.exit(-1);
    }
}
