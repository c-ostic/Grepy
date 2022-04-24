package edu.marist.costic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
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

    // input and output files for the program
    private File inputFile;
    private File nfaDotFile;
    private File dfaDotFile;

    private String regex;
    private List<String> inputStrings;
    private Set<Character> alphabet;

    public Utils() {
        inputStrings = new ArrayList<String>();
        alphabet = new HashSet<Character>();
    }

    /**
     * Getter for regex string.
     * @return the regex string
     */
    public String getRegex() {
        return regex;
    }

    /**
     * Getter for the list of input strings.
     * @return the list of input strings
     */
    public List<String> getInput() {
        return inputStrings;
    }

    /**
     * Getter for the file alphabet.
     * @return the alphabet of the input file
     */
    public Set<Character> getAlphabet() {
        return alphabet;
    }

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
        options.addOption("n", true, "The dot file to write the NFA to");
        options.addOption("d", true, "The dot file the write the DFA to");
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
        }

        if (cmd.hasOption("v")) {
            System.out.println("Verbose mode");
            System.out.println(CONSTANTS.getAppName()
                    + " version: "
                    + CONSTANTS.getVersion());
        }

        nfaDotFile = new File(cmd.getOptionValue("n", CONSTANTS.getDefaultNFAFile()));
        dfaDotFile = new File(cmd.getOptionValue("d", CONSTANTS.getDefaultDFAFile()));

        String[] otherArgs = cmd.getArgs();

        // make sure there is exactly 2 more arguments
        if (otherArgs.length < 2) {
            error("Missing arguments. Need one argument for regex and one for input file");
        } else if (otherArgs.length > 2) {
            error("Too many arguments. Need one argument for regex and one for input file");
        }

        regex = otherArgs[0];
        inputFile = new File(otherArgs[1]);

        processInput();

        return cmd;
    }

    /**
     * Process the input file to create the list of input strings and the alphabet.
     */
    private void processInput() {
        try {
            Scanner scan = new Scanner(inputFile);

            while (scan.hasNextLine()) {
                String nextLine = scan.nextLine();

                // add the next line to the list of input
                inputStrings.add(nextLine);

                // add the characters of the line to the alphabet set
                for (char c : nextLine.toCharArray()) {
                    alphabet.add(c);
                }
            }
            scan.close();

        } catch (FileNotFoundException e) {
            error(e.getMessage());
        }
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
